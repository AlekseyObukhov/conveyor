package com.credit.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Schema(name = "Request for a loan")
public class LoanApplicationRequestDTO {

    @NotNull
    @DecimalMin(value = "10000", message = "Amount cannot be less than 10000")
    @Schema(description = "Requested amount", example = "100000")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6, message = "Term cannot be less than 6")
    @Schema(description = "Loan term in months", example = "6")
    private Integer term;

    @NotEmpty
    @Pattern(regexp = "[A-Za-z]{2,30}", message = "Name can contain only Latin letters" +
            " and name length cannot be less than 2 characters or more then 30")
    @Schema(description = "Client's name", example = "Ivan")
    private String firstName;

    @NotEmpty
    @Pattern(regexp = "[A-Za-z]{2,30}", message = "Last name can contain only Latin letters" +
            " and length cannot be less than 2 characters or more then 30")
    @Schema(description = "Client's last name", example = "Ivanov")
    private String lastName;

    @Pattern(regexp = "[A-Za-z]{2,30}", message = "Middle name can contain only Latin letters" +
            " and length cannot be less than 2 characters or more then 30")
    @Schema(description = "Client's middle name", example = "Petrovich")
    private String middleName;

    @Pattern(regexp =  "\\w{2,50}@[\\w.]{2,20}", message = "Not valid email")
    @Schema(description = "Email", example = "ivanov@gmail.com")
    private String email;

    @NotNull
    @Past(message = "Date cannot be future")
    @Schema(description = "Birthdate", example = "1999-11-13")
    private LocalDate birthdate;

    @NotNull
    @Pattern(regexp = "[\\d]{4}", message = "Not valid passport series")
    @Schema(description = "Passport series", example = "1234")
    private String passportSeries;

    @NotNull
    @Pattern(regexp = "[\\d]{6}", message = "Not valid passport number")
    @Schema(description = "Passport number", example = "123456")
    private String passportNumber;
}