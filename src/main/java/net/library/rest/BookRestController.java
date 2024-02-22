package net.library.rest;

import net.library.dto.AdminUserDto;
import net.library.dto.BookDTO;
import net.library.model.Book;
import net.library.model.User;
import net.library.service.BookService;
import net.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/books/")
public class BookRestController {
    private final BookService bookService;


    @Autowired
    public BookRestController(BookService bookAdminService) {
        this.bookService = bookAdminService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        List<BookDTO> bookDTOs = books.stream()
                .map(BookDTO::fromBook)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(bookDTOs);
    }

    @GetMapping(value = "search/{title}")
    public ResponseEntity<BookDTO> getBookByTitle(@PathVariable(name = "title") String title) {
        Book book = bookService.getBookByTitle(title);
        if (book == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        BookDTO bookDTO = BookDTO.fromBook(book);
        return new ResponseEntity<>(bookDTO, HttpStatus.OK);
    }

    @PostMapping("/add-to-favorites/{bookId}")
    public ResponseEntity<String> addToFavorites(@PathVariable Long bookId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
        }

        String username = principal.getName();
        bookService.addBookToFavorites(username, bookId);
        return ResponseEntity.ok("Book added to favorites.");
    }

    @DeleteMapping("/delete-from-favorites/{bookId}")
    public ResponseEntity<String> deleteFromFavorites(@PathVariable Long bookId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
        }

        String username = principal.getName();
        bookService.deleteBookFromFavorites(username, bookId);
        return ResponseEntity.ok("Book deleted from favorites.");
    }

    @GetMapping("/my-books")
    public ResponseEntity<List<BookDTO>> getMyFavoriteBooks(Principal principal) {
        if (principal == null) {
            // Если пользователь не аутентифицирован, возвращаем ошибку 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = principal.getName();
        List<Book> favoriteBooks = bookService.getFavoriteBooksByUsername(username);
        List<BookDTO> bookDTOs = favoriteBooks.stream()
                .map(BookDTO::fromBook)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(bookDTOs);
    }
}
