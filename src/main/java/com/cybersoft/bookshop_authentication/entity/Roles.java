package com.cybersoft.bookshop_authentication.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "role")
    private List<Users> users;
}
