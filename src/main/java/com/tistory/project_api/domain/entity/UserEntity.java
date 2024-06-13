package com.tistory.project_api.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "user")
@EntityListeners(UserEntityListener.class)
@Getter
@Setter
public class UserEntity {

    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    @Column(nullable = false)
    private boolean enabled = true;
    private String role;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private String createdAt;
    @LastModifiedDate
    @Column(nullable = false)
    private String updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.updatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (this.role == null) {
            this.role = "user";
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
