package ru.glebov.spring_data_jdbc.Repository;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.glebov.spring_data_jdbc.model.Book;

import java.util.List;

@Repository
public class BookRepository {

    private static final RowMapper<Book> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Book.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertBook;

    public BookRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertBook = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("book")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Book save(Book book) {

        BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(book);

        if (book.getId() == null) {
            Number newKey = insertBook.executeAndReturnKey(beanPropertySqlParameterSource);
            book.setId(newKey.intValue());
            return book;
        } else {
            namedParameterJdbcTemplate.update("""
                       UPDATE book SET title=:title, author=:author,  
                       publication_year=:publicationYear WHERE id=:id
                    """, beanPropertySqlParameterSource);
            return null;
        }
    }

    public Book get(int id) {

        List<Book> users = jdbcTemplate.query("SELECT * FROM book WHERE id=?", ROW_MAPPER, id);
        return DataAccessUtils.singleResult(users);
    }

    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM book WHERE id=?", id) != 0;
    }

}
