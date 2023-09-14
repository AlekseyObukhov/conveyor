package com.credit.gateway.service;

import com.credit.gateway.dto.LoanApplicationRequestDTO;
import com.credit.gateway.dto.LoanOfferDTO;
import com.credit.gateway.feignclient.ApplicationFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationFeignClient applicationFeignClient;
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        List<LoanOfferDTO> loanOfferDTOList = applicationFeignClient.getOffers(loanApplicationRequestDTO).getBody();
        log.info("getOffers from application = {}", loanOfferDTOList);
        return loanOfferDTOList;
    }

    public void applyOffer(LoanOfferDTO loanOfferDTO) {
        log.info("applyOffer() loanOfferDTO = {}", loanOfferDTO);
        applicationFeignClient.applyOffer(loanOfferDTO);
    }
}
