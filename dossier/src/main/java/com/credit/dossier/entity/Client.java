package com.credit.dossier.entity;


import com.credit.dossier.model.Employment;
import com.credit.dossier.model.Passport;
import com.credit.dossier.model.enums.Gender;
import com.credit.dossier.model.enums.MaritalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    private Long id;

    private String firstName;

    private String lastName;

    private String middleName;

    private LocalDate birthDate;

    private String email;

    private Gender gender;

    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    private Passport passport;

    private Employment employment;

    private String account;
}
