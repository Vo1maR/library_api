package net.library.repository;

import net.library.model.Author;
import net.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByName(String name);
}
