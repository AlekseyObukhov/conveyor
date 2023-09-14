package com.credit.deal.dto;

import com.credit.deal.model.enums.Theme;
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
