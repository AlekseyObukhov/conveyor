package com.credit.conveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Запрос на получение кредита")
public class LoanApplicationRequestDTO {

    @NotNull
    @DecimalMin(value = "10000", message = "Amount cannot be less than 10000")
    @Schema(description = "Запрашиваемая сумма", example = "100000")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6, message = "Term cannot be less than 6")
    @Schema(description = "Срок займа в месяцах", example = "6")
    private Integer term;

    @NotEmpty
    @Pattern(regexp = "[A-Za-z]{2,30}", message = "Name can contain only Latin letters" +
            " and name length cannot be less than 2 characters or more then 30")
    @Schema(description = "Имя заемщика", example = "Ivan")
    private String firstName;

    @NotEmpty
    @Pattern(regexp = "[A-Za-z]{2,30}", message = "Last name can contain only Latin letters" +
            " and length cannot be less than 2 characters or more then 30")
    @Schema(description = "Фамилия заемщика", example = "Ivanov")
    private String lastName;

    @Pattern(regexp = "[A-Za-z]{2,30}", message = "Middle name can contain only Latin letters" +
            " and length cannot be less than 2 characters or more then 30")
    @Schema(description = "Отчество заемщика", example = "Petrovich")
    private String middleName;

    @Pattern(regexp =  "\\w{2,50}@[\\w.]{2,20}", message = "Not valid email")
    @Schema(description = "Электронная почта", example = "ivanov@gmail.com")
    private String email;

    @NotNull
    @Past(message = "Date cannot be future")
    @Schema(description = "Дата рождения", example = "1999-11-13")
    private LocalDate birthdate;

    @NotNull
    @Pattern(regexp = "[\\d]{4}", message = "Not valid passport series")
    @Schema(description = "Серия паспорта", example = "1234")
    private String passportSeries;

    @NotNull
    @Pattern(regexp = "[\\d]{6}", message = "Not valid passport number")
    @Schema(description = "Номер паспорта", example = "123456")
    private String passportNumber;
}
