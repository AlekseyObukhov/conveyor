package com.credit.gateway.feignclient;

import com.credit.gateway.dto.FinishRegistrationRequestDTO;
import com.credit.gateway.entity.Application;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(url = "http://localhost:8081/deal", name = "deal-client")
public interface DealFeignClient {

    @PutMapping("/calculate/{applicationId}")
    ResponseEntity<Void> calculateCredit(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO, @PathVariable Long applicationId);

    @PostMapping("/document/{applicationId}/send")
    ResponseEntity<Void> sendDocumentRequest(@PathVariable Long applicationId);

    @PostMapping("/document/{applicationId}/sign")
    ResponseEntity<Void> signDocumentRequest(@PathVariable Long applicationId);

    @PostMapping("/document/{applicationId}/code")
    ResponseEntity<Void> signDocuments(@PathVariable Long applicationId, @RequestBody Integer sesCode);

    @GetMapping("/admin/application/{applicationId}")
    Application getApplicationById(@PathVariable Long applicationId);

    @GetMapping("/admin/application")
    List<Application> getApplications();

}
