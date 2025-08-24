package com.cybersoft.bookshop_authentication.entity;

import com.cybersoft.bookshop_authentication.enumable.StatusUser;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private StatusUser status;
    private String email;
    private String password;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;


    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID().toString();
        if (this.status == null) {
            this.status = StatusUser.ACTIVE; // default
        }
    }
}
