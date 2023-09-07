package com.credit.conveyor.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Past;

import com.credit.conveyor.dto.enums.Gender;
import com.credit.conveyor.dto.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Schema(description = "Data for scoring")
public class ScoringDataDTO {
    @NotNull
    @DecimalMin(value = "10000", message = "Amount cannot be less than 10000")
    @Schema(description = "Amount", example = "10000")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6, message = "Term cannot be less than 6")
    @Schema(description = "Term", example = "6")
    private Integer term;

    @NotEmpty
    @Pattern(regexp = "[A-Za-z]{2,30}", message = "Name can contain only Latin letters" +
            " and name length cannot be less than 2 characters or more then 30")
    @Schema(description = "First name", example = "Ivan")
    private String firstName;

    @NotEmpty
    @Pattern(regexp = "[A-Za-z]{2,30}", message = "Last name can contain only Latin letters" +
            " and length cannot be less than 2 characters or more then 30")
    @Schema(description = "Last name", example = "Ivanov")
    private String lastName;

    @Pattern(regexp = "[A-Za-z]{2,30}", message = "Middle name can contain only Latin letters" +
            " and length cannot be less than 2 characters or more then 30")
    @Schema(description = "Middle name", example = "Ivanovich")
    private String middleName;

    @NotNull
    @Schema(description = "Gender", example = "MALE")
    private Gender gender;

    @NotNull
    @Past(message = "Date cannot be future")
    @Schema(description = "Birthdate", example = "1998-04-19")
    private LocalDate birthdate;

    @NotNull
    @Pattern(regexp = "[\\d]{4}", message = "Not valid passport series")
    @Schema(description = "Passport series", example = "2016")
    private String passportSeries;

    @NotNull
    @Pattern(regexp = "[\\d]{6}", message = "Not valid passport number")
    @Schema(description = "Passport number", example = "123456")
    private String passportNumber;

    @NotNull
    @Schema(description = "Passport issue date", example = "2019-11-15")
    private LocalDate passportIssueDate;

    @NotNull
    @Schema(description = "Passport issue branch", example = "FMS")
    private String passportIssueBranch;

    @NotNull
    @Schema(description = "Passport issue branch", example = "MARRIED")
    private MaritalStatus maritalStatus;

    @NotNull
    @Schema(description = "Number of dependents", example = "0")
    private Integer dependentAmount;

    @NotNull
    private EmploymentDTO employment;

    @NotNull
    @Schema(description = "Account", example = "45234365")
    private String account;

    @NotNull
    @Schema(description = "Insurance", example = "true")
    private Boolean isInsuranceEnabled;

    @NotNull
    @Schema(description = "Salary client", example = "true")
    private Boolean isSalaryClient;
}
