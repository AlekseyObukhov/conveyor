package com.credit.dossier.entity;

import com.credit.dossier.dto.LoanOfferDTO;
import com.credit.dossier.model.StatusHistory;
import com.credit.dossier.model.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    private Long id;

    private Client client;

    private Credit credit;

    private ApplicationStatus status;

    private LocalDateTime creationDate;

    private LoanOfferDTO appliedOffer;

    private LocalDateTime signDate;

    private Integer sesCode;

    private List<StatusHistory> statusHistory;
}

