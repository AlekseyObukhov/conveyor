package com.credit.gateway.entity;

import com.credit.gateway.dto.LoanOfferDTO;
import com.credit.gateway.model.StatusHistory;
import com.credit.gateway.model.enums.ApplicationStatus;
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

