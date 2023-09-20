package com.credit.gateway.feignclient;

import com.credit.gateway.dto.LoanApplicationRequestDTO;
import com.credit.gateway.dto.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;

@FeignClient(url = "http://application:8082/application", name = "application-client")
public interface ApplicationFeignClient {
    @PostMapping
    ResponseEntity<List<LoanOfferDTO>> getOffers(@RequestBody LoanApplicationRequestDTO request);

    @PutMapping("/offer")
    ResponseEntity<Void> applyOffer(@RequestBody LoanOfferDTO loanOfferDTO);

}