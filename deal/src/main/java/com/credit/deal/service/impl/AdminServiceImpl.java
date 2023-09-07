package com.credit.deal.service.impl;

import com.credit.deal.entity.Application;
import com.credit.deal.exception.DealException;
import com.credit.deal.repository.ApplicationRepository;
import com.credit.deal.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        return application;
    }
}
