package com.kakaopay.preexam.model.account;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity(name = "Account")
@Table(name = "accounts")
public class Account implements Serializable {
    private static final long serialVersionUID = 3597051251082801705L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status")
    private int status;

    @CreationTimestamp
    @Column(name = "signup_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime signupTime;
}
