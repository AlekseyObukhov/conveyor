package com.credit.conveyor.service;

import com.credit.conveyor.dto.CreditDTO;
import com.credit.conveyor.dto.LoanApplicationRequestDTO;
import com.credit.conveyor.dto.LoanOfferDTO;
import com.credit.conveyor.dto.ScoringDataDTO;

import java.util.List;

public interface ConveyorService {
    List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO requestDTO);
    CreditDTO calculateCredit(ScoringDataDTO scoringDataDTO);

}
