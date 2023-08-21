package com.credit.conveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;

@Data
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
