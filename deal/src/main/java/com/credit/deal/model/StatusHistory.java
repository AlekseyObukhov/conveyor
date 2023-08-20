package com.credit.deal.model;

import com.credit.deal.model.enums.ApplicationStatus;
import com.credit.deal.model.enums.ChangeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusHistory {

    private ApplicationStatus status;

    private LocalDateTime date;

    private ChangeType changeType;
}
