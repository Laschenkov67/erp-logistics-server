package com.example.demo.controllers;

import com.example.demo.entity.Emails;
import com.example.demo.entity.enums.EmailType;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.payload.request.EmailEntityRequest;
import com.example.demo.repository.EmailEntityRepository;
import com.example.demo.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class EmailController {

    private final String EMAIL_NOT_FOUND = "Such email doesn't exist!";

    private final EmailEntityRepository emailEntityRepository;
    private final EmailService emailService;

    @Autowired
    public EmailController(EmailEntityRepository emailEntityRepository, EmailService emailService) {
        this.emailEntityRepository = emailEntityRepository;
        this.emailService = emailService;
    }

    @GetMapping("/emails/inbox")
    @ResponseStatus(HttpStatus.OK)
    public List<Emails> readMail() {
        return emailService.readMail();
    }

    @GetMapping("/emails/outbox")
    @ResponseStatus(HttpStatus.OK)
    public List<Emails> readSentMail() {
        return emailEntityRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"))
                .stream()
                .filter(emailEntity -> emailEntity.getEmailType().equals(EmailType.SENT))
                .collect(Collectors.toList());
    }

    @PostMapping("/emails")
    @ResponseStatus(HttpStatus.CREATED)
    public Emails sendMailWithAddress(@RequestBody EmailEntityRequest request) {
        return emailService.sendMessage(request.getEmail(), request.getSubject(), request.getContent());
    }

    @GetMapping("/emails/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Emails> readConversation(@PathVariable("id") long id) {
        Emails emailEntity = emailEntityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        return emailEntityRepository.findByEmailOrderByTimestampDesc(emailEntity.getEmail())
                .orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
    }

    @PostMapping("/emails/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Emails sendMailWithAddrss(@RequestBody EmailEntityRequest request,
                                          @PathVariable("id") long id) {
        Emails emailEntity = emailEntityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        emailService.checkIfEmailReceived(emailEntity);
        return emailService.sendMessage(emailEntity.getEmail(), request.getSubject(),
                request.getContent());
    }

    @DeleteMapping("/emails/{id}")
    public HttpStatus removeEmail(@PathVariable("id") long id) {
        emailEntityRepository.delete(emailEntityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND)));
        return HttpStatus.NO_CONTENT;
    }
}
