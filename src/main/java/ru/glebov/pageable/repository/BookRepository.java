package ru.glebov.pageable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.glebov.pageable.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Book b WHERE b.id=:id")
    int delete(@Param("id") int id);

}
