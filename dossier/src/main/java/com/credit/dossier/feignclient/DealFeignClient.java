package com.credit.dossier.feignclient;

import com.credit.dossier.entity.Application;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://deal:8081/deal", name = "deal-client")
public interface DealFeignClient {
    @GetMapping("/admin/application/{applicationId}")
    Application getApplicationById(@PathVariable Long applicationId);

}
