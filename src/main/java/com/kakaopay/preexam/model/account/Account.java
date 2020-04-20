package com.kakaopay.preexam.model.account;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date signupTime;
}
