package ru.glebov.pageable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glebov.pageable.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
