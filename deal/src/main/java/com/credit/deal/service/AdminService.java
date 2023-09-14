package com.credit.deal.service;

import com.credit.deal.entity.Application;

import java.util.List;

public interface AdminService {
    Application getApplicationById(Long applicationId);

    List<Application> getApplications();

}
