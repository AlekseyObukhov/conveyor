package com.credit.deal.controller;

import com.credit.deal.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deal/document")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/{applicationId}/send")
    public ResponseEntity<Void> sendDocumentsRequest(@PathVariable Long applicationId) {
        emailService.sendDocumentRequest(applicationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{applicationId}/sign")
    public ResponseEntity<Void> signDocumentsRequest(@PathVariable Long applicationId) {
        emailService.signDocumentRequest(applicationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{applicationId}/code")
    public ResponseEntity<Void> signDocuments(@PathVariable Long applicationId, @RequestBody Integer sesCode) {
        emailService.signDocument(applicationId, sesCode);
        return ResponseEntity.ok().build();
    }
}
