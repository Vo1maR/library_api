package net.library.service;

import net.library.model.*;
import net.library.repository.AuthorRepository;
import net.library.repository.BookRepository;
import net.library.repository.GenreRepository;
import net.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class BookService {
    private final UserRepository userRepository;

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    @Autowired
    public BookService(BookRepository bookRepository,
                       AuthorRepository authorRepository,
                       GenreRepository genreRepository,
                       UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.userRepository = userRepository;
    }

    public void addBook(Book book) {
        book.setStatus(Status.ACTIVE);
        LocalDateTime localDateTime = LocalDateTime.now();
        book.setCreated(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        book.setUpdated(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        // Проходимся по авторам книги и сохраняем или обновляем каждого из них
        if (book.getAuthor() != null) {
            for (Author author : book.getAuthor()) {
                Author existingAuthor = authorRepository.findByName(author.getName());
                if (existingAuthor != null) {
                    // Если автор уже существует в базе данных, обновляем его данные
                    author.setId(existingAuthor.getId());
                } else {
                    author.setStatus(Status.ACTIVE);
                    author.setCreated(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
                    author.setUpdated(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
                    // Если автор новый, сохраняем его в базе данных
                    authorRepository.save(author);
                }
            }
        }

        if (book.getGenre() != null) {
            for (Genre genre : book.getGenre()) {
                Genre existingGenre = genreRepository.findByName(genre.getName());
                if (existingGenre != null) {
                    // Если автор уже существует в базе данных, обновляем его данные
                    genre.setId(existingGenre.getId());
                } else {
                    genre.setStatus(Status.ACTIVE);
                    genre.setCreated(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
                    genre.setUpdated(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
                    // Если автор новый, сохраняем его в базе данных
                    genreRepository.save(genre);
                }
            }
        }
        // Сохраняем книгу в базе данных
        bookRepository.save(book);
    }

    public void addBookToFavorites(String username, Long bookId) {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new EntityNotFoundException("User not found");

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        if (user.getFavoriteBooks().stream().anyMatch(b -> b.getId().equals(bookId))) {
            throw new EntityNotFoundException("Book is already added to favorites");
        }

        user.getFavoriteBooks().add(book);
        userRepository.save(user);
    }

    public void deleteBookFromFavorites(String username, Long bookId) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        if (!user.getFavoriteBooks().contains(book)) {
            throw new EntityNotFoundException("Book not found in favorites");
        }

        user.getFavoriteBooks().remove(book);
        userRepository.save(user);
    }


    public List<Book> getFavoriteBooksByUsername(String username) {
        if (userRepository.findByUsername(username) == null) throw new EntityNotFoundException("User not found");
        User user = userRepository.findByUsername(username);

        return user.getFavoriteBooks();
    }
    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }

    public Book getBookByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}

