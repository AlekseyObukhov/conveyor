package com.credit.gateway.controller;

import com.credit.gateway.dto.FinishRegistrationRequestDTO;
import com.credit.gateway.entity.Application;
import com.credit.gateway.service.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("/gateway")
@Tag(name = "Deal")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;
    @PutMapping("/calculate/{applicationId}")
    @Operation(summary = "Credit calculation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> calculateCredit(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO, @PathVariable Long applicationId) {
        dealService.calculateCredit(finishRegistrationRequestDTO, applicationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/document/{applicationId}/send")
    @Operation(summary = "Send documents request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> sendDocumentsRequest(@PathVariable Long applicationId) {
        dealService.sendDocumentsRequest(applicationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/document/{applicationId}/sign")
    @Operation(summary = "Sign documents request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> signDocumentsRequest(@PathVariable Long applicationId) {
        dealService.signDocumentRequest(applicationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/document/{applicationId}/code")
    @Operation(summary = "Send Simple Electric Sign code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> signDocuments(@PathVariable Long applicationId, Integer sesCode) {
        dealService.signDocument(applicationId, sesCode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/application/{applicationId}")
    @Operation(summary = "Get application by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Application not found", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Application> getApplicationById(@PathVariable Long applicationId) {
        return ResponseEntity.ok(dealService.getApplicationById(applicationId));
    }

    @GetMapping("/admin/application")
    @Operation(summary = "Get all applications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "No applications present", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<Application>> getAllApplication() {
        return  ResponseEntity.ok(dealService.getApplications());
    }
}
