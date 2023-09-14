package com.credit.dossier.service;

import com.credit.dossier.exception.EmailSendingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.io.File;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${email.sender}")
    private String senderEmail;

    public void sendMessage(String receiver, String text, String subject) {
        log.info("Sending message to {}: Subject={}, Text={}", receiver, subject, text);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);

        log.info("message send to {}", receiver);
    }

    public void sendDocument(String receiver, File file ) {
        try {
            log.info("Sending document to {}: File = {}", receiver, file.getName());
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setTo(receiver);
            mimeMessageHelper.setFrom(new InternetAddress(senderEmail, senderEmail));

            message.setSubject("Credit registration");
            Multipart multipart = new MimeMultipart();
            MimeBodyPart fileBodyPart = new MimeBodyPart();

            DataSource fileDataSource = new FileDataSource(file);
            fileBodyPart.setDataHandler(new DataHandler(fileDataSource));
            fileBodyPart.setFileName(file.getName() + ".txt");
            multipart.addBodyPart(fileBodyPart);

            message.setContent(multipart);
            javaMailSender.send(message);
            log.info("Document sent to {}", receiver);
        } catch (Exception e) {
            log.error("Error sending document: {}", e.getMessage());
            throw new EmailSendingException("Error sending document");
        }
    }

}
