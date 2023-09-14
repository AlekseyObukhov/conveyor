package com.credit.dossier.model;

import com.credit.dossier.model.enums.EmploymentStatus;
import com.credit.dossier.model.enums.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employment {

    private EmploymentStatus employmentStatus;

    private String employerINN;

    private BigDecimal salary;

    private Position position;

    private Integer workExperienceTotal;

    private Integer workExperienceCurrent;
}
