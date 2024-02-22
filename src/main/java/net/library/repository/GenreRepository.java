package net.library.repository;

import net.library.model.Author;
import net.library.model.Book;
import net.library.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findByName(String name);
}
