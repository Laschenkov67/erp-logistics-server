package com.example.demo.entity;
import com.example.demo.entity.enums.EmailType;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Emails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Email
    private String email;
    private String subject;

    @ElementCollection
    private List<String> content;

    @Enumerated(EnumType.STRING)
    private EmailType emailType;

    LocalDateTime timestamp;

    public Emails (@Email String email, String subject, List<String> content, EmailType emailType,
                       LocalDateTime timestamp) {
        this.email = email;
        this.subject = subject;
        this.content = content;
        this.emailType = emailType;
        this.timestamp = timestamp;
    }
}
