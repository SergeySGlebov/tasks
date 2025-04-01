package ru.glebov.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glebov.jwt.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
