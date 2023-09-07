package com.credit.dossier.entity;

import com.credit.dossier.dto.PaymentScheduleElement;
import com.credit.dossier.model.enums.CreditStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Credit {

    private Long id;

    private BigDecimal amount;

    private Integer term;

    private BigDecimal monthlyPayment;

    private BigDecimal rate;

    private BigDecimal psk;

    private List<PaymentScheduleElement> paymentSchedule;

    private Boolean isInsuranceEnabled;

    private Boolean isSalaryClient;

    private CreditStatus creditStatus;
}
