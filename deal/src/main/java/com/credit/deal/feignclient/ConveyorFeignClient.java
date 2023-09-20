package com.credit.deal.feignclient;

import com.credit.deal.dto.CreditDTO;
import com.credit.deal.dto.LoanApplicationRequestDTO;
import com.credit.deal.dto.LoanOfferDTO;
import com.credit.deal.dto.ScoringDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(url = "http://conveyor:8080/conveyor", name = "conveyor-client")
public interface ConveyorFeignClient {
    @PostMapping("/offers")
    ResponseEntity<List<LoanOfferDTO>> generateOffers(@RequestBody LoanApplicationRequestDTO request);

    @PostMapping("/calculation")
    ResponseEntity<CreditDTO> calculateCredit(@RequestBody ScoringDataDTO scoringData);
}
