package com.credit.application.controller;

import com.credit.application.dto.LoanApplicationRequestDTO;
import com.credit.application.dto.LoanOfferDTO;
import com.credit.application.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
@Tag(name = "Application")
public class ApplicationController {

    private final ApplicationService applicationService;
    @PostMapping
    @Operation(summary = "Get a list of loan offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan offers have been successfully received"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<List<LoanOfferDTO>> getOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return new ResponseEntity<>(applicationService.getOffers(loanApplicationRequestDTO), HttpStatus.OK);
    }

    @PutMapping("/offer")
    @Operation(summary = "Select and save an offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer successfully applied and saved"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> applyOffer(@RequestBody LoanOfferDTO loanOfferDTO) {
        applicationService.applyOffer(loanOfferDTO);
        return ResponseEntity.ok().build();
    }
}
