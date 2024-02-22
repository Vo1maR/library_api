package net.library.repository;

import net.library.model.Book;
import net.library.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByTitle(String title);
}
