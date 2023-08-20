package com.credit.deal.controller;

import com.credit.deal.dto.FinishRegistrationRequestDTO;
import com.credit.deal.dto.LoanApplicationRequestDTO;
import com.credit.deal.dto.LoanOfferDTO;
import com.credit.deal.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
@Tag(name = "Deal")
public class DealController {

    private final DealService dealService;

    @PostMapping("/application")
    @Operation(summary = "Get a list of loan offers")
    public ResponseEntity<List<LoanOfferDTO>> getOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return new ResponseEntity<>(dealService.getOffers(loanApplicationRequestDTO), HttpStatus.OK);
    }

    @PutMapping("/offer")
    @Operation(summary = "Select and save an offer")
    public ResponseEntity<Void> applyOffer(@RequestBody LoanOfferDTO loanOfferDTO) {
        dealService.applyOffer(loanOfferDTO);
        return  ResponseEntity.ok().build();
    }

    @PutMapping("/calculate/{applicationId}")
    @Operation(summary = "Credit calculation")
    public ResponseEntity<Void> calculateCredit(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO, @PathVariable Long applicationId) {
        dealService.calculateCredit(finishRegistrationRequestDTO, applicationId);
        return  ResponseEntity.ok().build();
    }
}
