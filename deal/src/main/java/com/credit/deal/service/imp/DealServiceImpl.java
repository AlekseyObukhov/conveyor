package com.credit.deal.service.imp;

import com.credit.deal.dto.FinishRegistrationRequestDTO;
import com.credit.deal.dto.LoanApplicationRequestDTO;
import com.credit.deal.dto.LoanOfferDTO;
import com.credit.deal.dto.ScoringDataDTO;
import com.credit.deal.dto.CreditDTO;
import com.credit.deal.entity.Application;
import com.credit.deal.entity.Client;
import com.credit.deal.entity.Credit;
import com.credit.deal.exception.DealException;
import com.credit.deal.feignclient.DealFeignClient;
import com.credit.deal.model.Employment;
import com.credit.deal.model.StatusHistory;
import com.credit.deal.model.enums.ApplicationStatus;
import com.credit.deal.model.enums.CreditStatus;
import com.credit.deal.repository.ApplicationRepository;
import com.credit.deal.repository.ClientRepository;
import com.credit.deal.repository.CreditRepository;
import com.credit.deal.service.DealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

    private final DealFeignClient dealFeignClient;
    private final ApplicationRepository applicationRepository;
    private final ClientRepository clientRepository;
    private final CreditRepository creditRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Method getOffers starts with requestDTO = {}", loanApplicationRequestDTO);

        Client client = modelMapper.map(loanApplicationRequestDTO, Client.class);

        log.info("Create new client = {}", client);

        clientRepository.save(client);
        log.info("Save client in db {}", client);

        Application application = Application.builder()
                .client(client).creationDate(LocalDate.now())
                .build();
        updateApplicationStatus(application, ApplicationStatus.PREAPPROVAL);
        log.debug("Create new application = {}", application);

        applicationRepository.save(application);
        log.info("save application in db {}", application);

        List<LoanOfferDTO> loanOfferDTO = dealFeignClient.generateOffers(loanApplicationRequestDTO);
        log.info("Method getOffers return loanOfferDTO = {}", loanOfferDTO);
        return loanOfferDTO;
    }

    @Override
    public void applyOffer(LoanOfferDTO loanOfferDTO) {
        log.info("Method applyOffer start with loanOfferDTO = {}", loanOfferDTO);
        Optional<Application> applicationOptional = applicationRepository.findById(loanOfferDTO.getApplicationId());

        Application application;
        if (applicationOptional.isPresent()) {
            application = applicationOptional.get();
        } else {
            throw new DealException("Application id not found");
        }
        log.debug("Application byId = {}", application);

        updateApplicationStatus(application, ApplicationStatus.APPROVED);

        application.setAppliedOffer(loanOfferDTO);

        applicationRepository.save(application);
        log.info("save application in db{}", application);
    }

    @Override
    public void calculateCredit(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {
        log.info("Method calculateCredit starts with finishRegistrationRequestDTO {}, id = {}", finishRegistrationRequestDTO, applicationId);
        Optional<Application> applicationOptional = applicationRepository.findById(applicationId);

        Application application;
        if (applicationOptional.isPresent()) {
            application = applicationOptional.get();
        } else {
            throw new DealException("Application id not found");
        }

        Employment employment = modelMapper.map(finishRegistrationRequestDTO.getEmployment(), Employment.class);

        Client client = application.getClient();
        client.setGender(finishRegistrationRequestDTO.getGender());
        client.setMaritalStatus(finishRegistrationRequestDTO.getMaritalStatus());
        client.setDependentAmount(finishRegistrationRequestDTO.getDependentAmount());
        client.setEmployment(employment);
        client.setAccount(finishRegistrationRequestDTO.getAccount());
        clientRepository.save(client);
        log.info("save client in db {}", client);

        ScoringDataDTO scoringDataDTO = fillScoringData(finishRegistrationRequestDTO, application, client);

        CreditDTO creditDTO = dealFeignClient.calculateCredit(scoringDataDTO);

        Credit credit = modelMapper.map(creditDTO, Credit.class);
        log.debug("map credit from creditDTO {}", credit);

        credit.setCreditStatus(CreditStatus.CALCULATED);

        creditRepository.save(credit);
        log.info("save credit in database {}", credit);

        application.setCredit(credit);
        log.debug("set credit in application {}", application);

        updateApplicationStatus(application, ApplicationStatus.CC_APPROVED);

        applicationRepository.save(application);
        log.info("save application in database {}", application);

    }

    private ScoringDataDTO fillScoringData(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Application application, Client client) {
        return ScoringDataDTO.builder()
                .amount(application.getAppliedOffer().getTotalAmount())
                .term(application.getAppliedOffer().getTerm())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .gender(finishRegistrationRequestDTO.getGender())
                .birthdate(client.getBirthDate())
                .passportSeries(client.getPassport().getSeries().toString())
                .passportNumber(client.getPassport().getNumber().toString())
                .passportIssueDate(finishRegistrationRequestDTO.getPassportIssueDate())
                .passportIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch())
                .maritalStatus(finishRegistrationRequestDTO.getMaritalStatus())
                .dependentAmount(finishRegistrationRequestDTO.getDependentAmount())
                .employment(finishRegistrationRequestDTO.getEmployment())
                .account(finishRegistrationRequestDTO.getAccount())
                .isInsuranceEnabled(application.getAppliedOffer().getIsInsuranceEnabled())
                .isSalaryClient(application.getAppliedOffer().getIsSalaryClient())
                .build();
    }

    private void updateApplicationStatus(Application application, ApplicationStatus newStatus) {
        application.setStatus(newStatus);
        log.info("application status change on {}", newStatus);
        StatusHistory statusHistory = StatusHistory.builder()
                .date(LocalDate.now())
                .status(newStatus)
                .build();
        if (application.getStatusHistory() == null) {
            application.setStatusHistory(new ArrayList<>());
        }
        application.getStatusHistory().add(statusHistory);
    }
}

