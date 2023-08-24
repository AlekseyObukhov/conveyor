package com.credit.conveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Loan offer")
public class LoanOfferDTO {

    @Schema(description = "Credit offer number", example = "1")
    private Long applicationId;

    @Schema(description = "Requested amount", example = "100000")
    private BigDecimal requestedAmount;

    @Schema(description = "Total amount", example = "100000")
    private BigDecimal totalAmount;

    @Schema(description = "Loan term in months", example = "6")
    private Integer term;

    @Schema(description = "Monthly payment", example = "17417.84")
    private BigDecimal monthlyPayment;

    @Schema(description = "Credit rate", example = "5")
    private BigDecimal rate;

    @Schema(description = "Insurance", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Salary client", example = "true")
    private Boolean isSalaryClient;
}
