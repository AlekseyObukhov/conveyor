package com.credit.application.feignclient;

import com.credit.application.dto.LoanApplicationRequestDTO;
import com.credit.application.dto.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@FeignClient(url = "http://localhost:8081/deal", name = "deal-client")
public interface DealFeignClient {
    @PostMapping("/application")
    ResponseEntity<List<LoanOfferDTO>> getOffers(@RequestBody LoanApplicationRequestDTO request);

    @PutMapping("/offer")
    ResponseEntity<Void> applyOffer(@RequestBody LoanOfferDTO request);
}
