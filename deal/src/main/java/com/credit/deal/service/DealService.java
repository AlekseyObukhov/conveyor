package com.credit.deal.service;

import com.credit.deal.dto.FinishRegistrationRequestDTO;
import com.credit.deal.dto.LoanApplicationRequestDTO;
import com.credit.deal.dto.LoanOfferDTO;

import java.util.List;

public interface DealService {

    List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequestDTO);

    void applyOffer(LoanOfferDTO loanOfferDTO);

    void calculateCredit(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId);
}
