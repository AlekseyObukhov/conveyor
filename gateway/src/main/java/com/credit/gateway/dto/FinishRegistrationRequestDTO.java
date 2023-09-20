package com.credit.gateway.dto;

import com.credit.gateway.model.enums.Gender;
import com.credit.gateway.model.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "Finish registration request", description = "Finish registration request with full client info")
public class FinishRegistrationRequestDTO {

    @Schema(description = "Client's gender", example = "MALE")
    private Gender gender;

    @Schema(description = "Client's marital status", example = "SINGLE")
    private MaritalStatus maritalStatus;

    @Schema(description = "Number of dependents", example = "0")
    private Integer dependentAmount;

    @Schema(description = "Client's passport issue date", example = "2023-09-14")
    private LocalDate passportIssueDate;

    @Schema(description = "Client's passport issue branch", example = "123456")
    private String passportIssueBranch;

    private EmploymentDTO employment;

    @Schema(description = "Client's account", example = "account")
    private String account;
}
