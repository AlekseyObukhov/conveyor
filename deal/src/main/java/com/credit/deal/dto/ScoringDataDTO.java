package com.credit.deal.dto;

import com.credit.deal.model.enums.Gender;
import com.credit.deal.model.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoringDataDTO {
    @NotNull
    @DecimalMin(value = "10000", message = "Amount cannot be less than 10000")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6, message = "Term cannot be less than 6")
    private Integer term;

    @NotEmpty
    @Pattern(regexp = "[A-Za-z]{2,30}", message = "Name can contain only Latin letters" +
            " and name length cannot be less than 2 characters or more then 30")
    private String firstName;

    @NotEmpty
    @Pattern(regexp = "[A-Za-z]{2,30}", message = "Last name can contain only Latin letters" +
            " and length cannot be less than 2 characters or more then 30")
    private String lastName;

    @Pattern(regexp = "[A-Za-z]{2,30}", message = "Middle name can contain only Latin letters" +
            " and length cannot be less than 2 characters or more then 30")
    private String middleName;

    @NotNull
    private Gender gender;

    @NotNull
    @Past(message = "Date cannot be future")
    @Schema(description = "Birthdate", example = "1999-11-13")
    private LocalDate birthdate;

    @NotNull
    @Pattern(regexp = "[\\d]{4}", message = "Not valid passport series")
    private String passportSeries;

    @NotNull
    @Pattern(regexp = "[\\d]{6}", message = "Not valid passport number")
    private String passportNumber;

    @NotNull
    private LocalDate passportIssueDate;

    @NotNull
    private String passportIssueBranch;

    @NotNull
    private MaritalStatus maritalStatus;

    @NotNull
    private Integer dependentAmount;

    @NotNull
    private EmploymentDTO employment;

    @NotNull
    private String account;

    @NotNull
    private Boolean isInsuranceEnabled;

    @NotNull
    private Boolean isSalaryClient;

}
