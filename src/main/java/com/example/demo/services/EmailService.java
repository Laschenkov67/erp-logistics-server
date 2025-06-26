package com.example.demo.services;


import com.example.demo.entity.Complaint;
import com.example.demo.entity.Emails;
import com.example.demo.entity.Order;
import com.example.demo.entity.Return;
import com.example.demo.entity.enums.EmailType;
import com.example.demo.exceptions.InvalidRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.repository.EmailEntityRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ReturnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    private final String FOOTER = "\n\n This is an automatically generated message. Please do not respond." +
            "\n\n Sincerely, ERP-MES Inc.";
    private final String NOT_FOUND = "Could not find requested entity!";

    @Autowired
    private JavaMailSender javaMailSender;

    private final EmailEntityRepository emailEntityRepository;
    private final OrderRepository orderRepository;
    private final ReturnRepository returnRepository;
    private final ComplaintRepository complaintRepository;

    @Autowired
    public EmailService(EmailEntityRepository emailEntityRepository,
                        OrderRepository orderRepository, ReturnRepository returnRepository,
                        ComplaintRepository complaintRepository) {
        this.emailEntityRepository = emailEntityRepository;
        this.orderRepository = orderRepository;
        this.returnRepository = returnRepository;
        this.complaintRepository = complaintRepository;
    }

    public void sensNewOrderRegisteredMessage(long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        String subject = "New order registered.";
        String details = "Hello, " + order.getFirstName() + "! \n\n We have successfully registered " +
                "your new order (id: " + order.getId() + "). Its current status is: " + order.getStatus() +
                "." + FOOTER;
        sendMessage(order.getEmail(), subject, Collections.singletonList(details));
    }

    public void sendOrderStatusChangeMessage(long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        String subject = "Status change for order " + order.getId();
        String details = "Hello, " + order.getFirstName() + "! \n\n The state of your order (id: " +
                order.getId() + ") has been changed to " + status + "." + FOOTER;
        sendMessage(order.getEmail(), subject, Collections.singletonList(details));
    }

    public void sendNewReturnRegisteredMessage(long id) {
        Return r = returnRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        String subject = "New return registered.";
        String details = "Hello, " + r.getFirstName() + "! \n\n We have successfully registered " +
                "your new return (id: " + r.getId() + "). Its current status is: " + r.getStatus() +
                "." + FOOTER;
        sendMessage(r.getEmail(), subject, Collections.singletonList(details));
    }

    public void sendReturnStatusChangeMessage(long id, String status) {
        Return r = returnRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        String subject = "Status change for return " + r.getId();
        String details = "Hello, " + r.getFirstName() + "! \n\n The state of your return (id: " +
                r.getId() + ") has been changed to " + status + "." + FOOTER;
        sendMessage(r.getEmail(), subject, Collections.singletonList(details));
    }

    public void sensNewComplaintRegisteredMessage(long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        String subject = "New complaint registered.";
        String details = "Hello, " + complaint.getFirstName() + "! \n\n We have successfully registered " +
                "your new complaint (id: " + complaint.getId() + "). Its current status is: " +
                complaint.getStatus() + "." + FOOTER;
        sendMessage(complaint.getEmail(), subject, Collections.singletonList(details));
    }

    public void sendComplaintStatusChangeMessage(long id, String status) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        String subject = "Status change for complaint " + complaint.getId();
        String details = "Hello, " + complaint.getFirstName() + "! \n\n The state of your complaint (id: " +
                complaint.getId() + ") has been changed to " + status + ". ";
        if (status.equals("IN_PROGRESS")) {
            details += "You will get your resolution details in another message";
        }
        details += FOOTER;
        sendMessage(complaint.getEmail(), subject, Collections.singletonList(details));
    }

    public void sendComplaintResolutionChangeMessage(long id, String resolution) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        String subject = "Resolution change for complaint " + complaint.getId();
        String details = "Hello, " + complaint.getFirstName() + "! \n\n The resolution of your complaint (id: " +
                complaint.getId() + ") will be: " + resolution + "." + FOOTER;
        sendMessage(complaint.getEmail(), subject, Collections.singletonList(details));
    }

    public Emails sendMessage(String to, String subject, List<String> content) {
        SimpleMailMessage message = new SimpleMailMessage();

        StringBuilder text = new StringBuilder();
        for (String part: content) {
            text.append(part);
        }
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text.toString());

        Emails emailEntity = new Emails(to, subject, content, EmailType.SENT,
                LocalDateTime.now());
        emailEntityRepository.save(emailEntity);

        try {
            javaMailSender.send(message);
        } catch (MailException me) {
            me.printStackTrace();
        }
        return emailEntity;
    }

    public List<Emails> readMail() {
        emailEntityRepository.findAll().forEach(emailEntity -> {
            if (emailEntity.getEmailType().equals(EmailType.RECEIVED)) {
                emailEntityRepository.delete(emailEntity);
            }
        });
        InboxService inboxService = new InboxService();
        List<Emails> emailEntities = inboxService.readMail();
        emailEntities.forEach(emailEntityRepository::save);
        return emailEntityRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"))
                .stream()
                .filter(emailEntity -> emailEntity.getEmailType().equals(EmailType.RECEIVED))
                .collect(Collectors.toList());
    }

    public void checkIfEmailReceived(Emails emailEntity) {
        if (emailEntity.getEmailType().equals(EmailType.SENT)) {
            throw new InvalidRequestException("Don't attempt to reply to yourself!");
        }
    }
}
