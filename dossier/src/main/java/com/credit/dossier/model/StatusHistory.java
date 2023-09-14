package com.credit.dossier.model;

import com.credit.dossier.model.enums.ApplicationStatus;
import com.credit.dossier.model.enums.ChangeType;
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
