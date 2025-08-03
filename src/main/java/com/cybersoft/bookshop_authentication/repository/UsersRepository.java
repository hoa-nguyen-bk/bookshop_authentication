package com.cybersoft.bookshop_authentication.repository;

import com.cybersoft.bookshop_authentication.entity.Users;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {
    Optional<Users> findByEmail(String email);

    boolean existsByEmail(@Email(message = "Invalid email format") String email);
}
