package com.credit.deal.feignclient;

import com.credit.deal.dto.CreditDTO;
import com.credit.deal.dto.LoanApplicationRequestDTO;
import com.credit.deal.dto.LoanOfferDTO;
import com.credit.deal.dto.ScoringDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(url = "http://localhost:8080/conveyor", name = "conveyor-application-client")
public interface DealFeignClient {
    @PostMapping("/offers")
    List<LoanOfferDTO> generateOffers(@RequestBody LoanApplicationRequestDTO request);

    @PostMapping("/calculation")
    CreditDTO calculateCredit(@RequestBody ScoringDataDTO scoringData);
}
