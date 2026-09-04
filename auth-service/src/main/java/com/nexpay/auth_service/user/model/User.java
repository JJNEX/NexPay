package com.nexpay.auth_service.user.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private Boolean active = true;

    private LocalDateTime lastLogin;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Role role;

    protected User() {
}

public User(
        String name,
        String cpf,
        String email,
        String password
) {
    this.name = name;
    this.cpf = cpf;
    this.email = email;
    this.password = password;
    this.role = Role.CUSTOMER;
    this.active = true;
}
}
