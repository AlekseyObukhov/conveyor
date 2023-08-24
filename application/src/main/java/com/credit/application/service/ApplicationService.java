package com.credit.application.service;

import com.credit.application.dto.LoanApplicationRequestDTO;
import com.credit.application.dto.LoanOfferDTO;

import java.util.List;

public interface ApplicationService {
    void applyOffer(LoanOfferDTO loanOfferDTO);

    List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequestDTO);
}
