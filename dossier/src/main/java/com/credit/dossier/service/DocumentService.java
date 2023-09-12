package com.credit.dossier.service;

import com.credit.dossier.entity.Application;
import com.credit.dossier.entity.Client;
import com.credit.dossier.entity.Credit;
import com.credit.dossier.exception.DocumentCreationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DocumentService {
    public List<File> createFiles(Application application) {
        File creditFile = createCreditFile(application.getCredit());
        File applicationFile = createApplicationFile(application);
        File clientFile = createClientFile(application.getClient());
        return List.of(creditFile, applicationFile, clientFile);
    }

    private File createCreditFile(Credit credit) {
        Map<String, String> data = new HashMap<>();
        data.put("creditAmount", credit.getAmount().toString());
        data.put("creditTerm", credit.getTerm().toString());
        data.put("monthlyPayment", credit.getMonthlyPayment().toString());
        data.put("rate", credit.getRate().toString());
        data.put("psk", credit.getPsk().toString());
        data.put("paymentSchedule", credit.getPaymentSchedule().toString());

        List<String> creditData = formatData(data);

        return writeFile("Credit", creditData);
    }

    private File createApplicationFile(Application application) {
        Map<String, String> data = new HashMap<>();
        data.put("Client", application.getClient().toString());
        data.put("creation date", application.getCreationDate().toString());
        data.put("apply offer", application.getAppliedOffer().toString());

        List<String> applicationData = formatData(data);

        return writeFile("Application", applicationData);
    }

    private File createClientFile(Client client) {
        Map<String, String> data = new HashMap<>();
        data.put("clientFullName", getClientFullName(client));
        data.put("birthday", client.getBirthDate().toString());
        data.put("email", client.getEmail());
        data.put("gender", client.getGender().toString());
        data.put("marital status", client.getMaritalStatus().toString());
        data.put("dependent amount", client.getDependentAmount().toString());
        data.put("passport", client.getPassport().toString());
        data.put("employment", client.getEmployment().toString());
        data.put("account", client.getAccount());

        List<String> clientData = formatData(data);

        return writeFile("Client", clientData);
    }

    private List<String> formatData(Map<String, String> data) {
        return data.entrySet()
                .stream()
                .map(e -> e.getKey() + " : " + e.getValue())
                .collect(Collectors.toList());
    }

    private File writeFile(String prefix, List<String> data) {
        try {
            Path tempFile = Files.createTempFile(prefix, ".txt");
            Files.write(tempFile, data);
            log.info("Created {} file: {}", prefix, tempFile);
            return tempFile.toFile();
        } catch (IOException e) {
            log.error("Error creating {} file: {}", prefix, e.getMessage());
            throw new DocumentCreationException("Error creating " + prefix + " file");
        }
    }

    private String getClientFullName(Client client) {
        return String.format("%s %s %s", client.getFirstName(), client.getLastName(), client.getMiddleName());
    }
}
