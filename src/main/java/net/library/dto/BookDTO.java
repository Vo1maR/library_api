package net.library.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import net.library.model.Author;
import net.library.model.Book;
import net.library.model.Genre;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDTO {
    private Long id;
    private String title;
    private Set<String> authors;
    private Set<String> genres;

    public static BookDTO fromBook(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());

        // Получаем имена авторов книги
        if (book.getAuthor() != null) {
            Set<String> authorNames = book.getAuthor().stream()
                    .map(Author::getName)
                    .collect(Collectors.toSet());
            bookDTO.setAuthors(authorNames);
        }

        // Получаем названия жанров книги
        if (book.getGenre() != null) {
            Set<String> genreNames = book.getGenre().stream()
                    .map(Genre::getName)
                    .collect(Collectors.toSet());
            bookDTO.setGenres(genreNames);
        }

        return bookDTO;
    }
}
