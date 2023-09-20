package com.credit.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "Payment schedule element", description = "Payment schedule element")
public class PaymentScheduleElement {

    @Schema(description = "Payment number", example = "1")
    private Integer number;

    @Schema(description = "Payment date", example = "2023-09-01")
    private LocalDate date;

    @Schema(description = "Total payment", example = "17417.84")
    private BigDecimal totalPayment;

    @Schema(description = "Interest payment", example = "429.20")
    private BigDecimal interestPayment;

    @Schema(description = "Debt payment", example = "16988.64")
    private BigDecimal debtPayment;

    @Schema(description = "Remaining payment", example = "86011.36")
    private BigDecimal remainingDebt;
}
