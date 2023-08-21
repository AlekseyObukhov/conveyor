package com.credit.deal.dto;

import com.credit.deal.model.enums.EmploymentStatus;
import com.credit.deal.model.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Employment data")
public class EmploymentDTO {

    @Schema(description = "Employment status", example = "EMPLOYED")
    private EmploymentStatus employmentStatus;

    @Schema(description = "Employer INN", example = "123456")
    private String employerINN;

    @Schema(description = "Salary", example = "80000")
    private BigDecimal salary;

    @Schema(description = "Position", example = "WORKER")
    private Position position;

    @Schema(description = "General work experience", example = "18")
    private Integer workExperienceTotal;

    @Schema(description = "Current work experience", example = "5")
    private Integer workExperienceCurrent;

}
