package com.credit.dossier.dto;

import com.credit.dossier.model.enums.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailMessage {

    private String address;
    private Theme theme;
    private Long applicationId;
}
