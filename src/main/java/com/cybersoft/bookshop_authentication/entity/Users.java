package com.cybersoft.bookshop_authentication.entity;

import com.cybersoft.bookshop_authentication.enumable.StatusUser;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Users {

    @Id
    private String id;

    private StatusUser status;
    private String email;
    private String password;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID().toString();
    }
}
