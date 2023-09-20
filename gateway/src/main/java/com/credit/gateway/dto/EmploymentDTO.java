package com.credit.gateway.dto;

import com.credit.gateway.model.enums.EmploymentStatus;
import com.credit.gateway.model.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "Employment", description = "Employment info")
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
