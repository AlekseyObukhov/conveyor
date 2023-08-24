package com.credit.conveyor.controller;

import com.credit.conveyor.dto.CreditDTO;
import com.credit.conveyor.dto.LoanApplicationRequestDTO;
import com.credit.conveyor.dto.LoanOfferDTO;
import com.credit.conveyor.dto.ScoringDataDTO;
import com.credit.conveyor.service.ConveyorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/conveyor")
@RequiredArgsConstructor
@Tag(name = "Credit calculator")
public class ConveyorController {

    private final ConveyorService conveyorService;

    @PostMapping("/offers")
    @Operation(summary = "Generating loan offers")
    public ResponseEntity<List<LoanOfferDTO>> getOffers(@Valid @RequestBody LoanApplicationRequestDTO requestDTO) {
        return new ResponseEntity<>(conveyorService.getOffers(requestDTO), HttpStatus.OK);
    }

    @PostMapping("/calculation")
    @Operation(summary = "Credit calculation")
    public ResponseEntity<CreditDTO> calculateCredit(@Valid @RequestBody ScoringDataDTO requestDTO) {
        return new ResponseEntity<>(conveyorService.calculateCredit(requestDTO), HttpStatus.OK);
    }
}
