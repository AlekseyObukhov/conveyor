package com.credit.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "Credit data", description = "Credit info")
public class CreditDTO {

    @Schema(description = "Requested amount", example = "103000")
    private BigDecimal amount;

    @Schema(description = "Requested term", example = "6")
    private Integer term;

    @Schema(description = "Monthly payment", example = "17417.84")
    private BigDecimal monthlyPayment;

    @Schema(description = "Credit rate", example = "5")
    private BigDecimal rate;

    @Schema(description = "Credit full price", example = "3")
    private BigDecimal psk;

    @Schema(description = "Is insurance enabled?", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Is salary client?", example = "true")
    private Boolean isSalaryClient;

    private List<PaymentScheduleElement> paymentSchedule;
}
