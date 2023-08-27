package com.credit.application.service.impl;

import com.credit.application.dto.LoanApplicationRequestDTO;
import com.credit.application.dto.LoanOfferDTO;
import com.credit.application.exception.PreScoringException;
import com.credit.application.feignclient.DealFeignClient;
import com.credit.application.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private static final String NAME_REGEX = "[a-zA-Z]{2,30}";
    private static final String EMAIL_REGEX = "\\w{2,50}@[\\w.]{2,20}";
    private static final String PASSPORT_NUMBER_REGEX = "\\d{6}";
    private static final String PASSPORT_SERIES_REGEX = "\\d{4}";
    private static final BigDecimal MIN_CREDIT_AMOUNT = BigDecimal.valueOf(10000);
    private static final int MIN_CREDIT_TERM = 6;
    private static final int MIN_CLIENT_AGE = 18;

    private final DealFeignClient dealFeignClient;

    @Override
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Method getOffers starts with loanApplicationRequestDTO = {}", loanApplicationRequestDTO);
        preScoring(loanApplicationRequestDTO);
        List<LoanOfferDTO> loanOfferDTOList = dealFeignClient.getOffers(loanApplicationRequestDTO).getBody();
        log.info("dealFeignClient return list of loan offers {}", loanOfferDTOList);
        return loanOfferDTOList;
    }

    @Override
    public void applyOffer(LoanOfferDTO loanOfferDTO) {
        log.info("Method applyOffer() starts with loanOfferDTO = {}", loanOfferDTO);
        dealFeignClient.applyOffer(loanOfferDTO);
        log.info("offer {} was successfully accepted", loanOfferDTO);
    }

    private void preScoring(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        List<String> exceptionInfo = new ArrayList<>();
        log.info("Start prescoring for client {} {} {}", loanApplicationRequestDTO.getLastName(),
                loanApplicationRequestDTO.getFirstName(), loanApplicationRequestDTO.getMiddleName());

        if (!loanApplicationRequestDTO.getFirstName().matches(NAME_REGEX)) {
            exceptionInfo.add("Incorrect first name");
        }
        if (!loanApplicationRequestDTO.getLastName().matches(NAME_REGEX)) {
            exceptionInfo.add("Incorrect last name");
        }
        if (loanApplicationRequestDTO.getMiddleName() != null && !loanApplicationRequestDTO.getMiddleName().matches(NAME_REGEX)) {
            exceptionInfo.add("Incorrect middle name");
        }
        if (!loanApplicationRequestDTO.getEmail().matches(EMAIL_REGEX)) {
            exceptionInfo.add("Client's email has incorrect format.");
        }
        if (!loanApplicationRequestDTO.getPassportSeries().matches(PASSPORT_SERIES_REGEX)) {
            exceptionInfo.add("Client's passport series has incorrect format.");
        }
        if (!loanApplicationRequestDTO.getPassportNumber().matches(PASSPORT_NUMBER_REGEX)) {
            exceptionInfo.add("passport number has incorrect format.");
        }
        if (loanApplicationRequestDTO.getAmount().compareTo(MIN_CREDIT_AMOUNT) < 0) {
            exceptionInfo.add("Credit amount less than 10000.");
        }
        if (loanApplicationRequestDTO.getTerm() < MIN_CREDIT_TERM) {
            exceptionInfo.add("Credit term less than 6.");
        }
        if (Period.between(loanApplicationRequestDTO.getBirthdate(), LocalDate.now()).getYears() < MIN_CLIENT_AGE) {
            exceptionInfo.add("Client too young");
        }
        if (!exceptionInfo.isEmpty()) {
            log.error("Pre-scoring failed by {}", exceptionInfo);
            throw new PreScoringException(exceptionInfo.toString());
        }
        else {
            log.info("Prescoring has been successfully completed");
        }
    }
}
