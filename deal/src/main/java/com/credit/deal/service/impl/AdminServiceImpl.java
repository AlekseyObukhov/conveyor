package com.credit.deal.service.impl;

import com.credit.deal.entity.Application;
import com.credit.deal.exception.DealException;
import com.credit.deal.repository.ApplicationRepository;
import com.credit.deal.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ApplicationRepository applicationRepository;

    @Override
    public Application getApplicationById(Long applicationId) {
        Optional<Application> applicationOptional = applicationRepository.findById(applicationId);

        Application application;
        if (applicationOptional.isPresent()) {
            application = applicationOptional.get();
        } else {
            throw new DealException("Application id not found");
        }
        log.info("get application from db: {}", application);
        return application;
    }

    @Override
    public List<Application> getApplications() {
        List<Application> applications = applicationRepository.findAll();
        log.info("get list applications from db: {}", applications);
        return applications;
    }

}
