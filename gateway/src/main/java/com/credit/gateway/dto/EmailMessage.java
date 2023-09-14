package com.credit.gateway.dto;

import com.credit.gateway.model.enums.Theme;
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
