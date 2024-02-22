package net.library.rest;

import net.library.model.Book;
import net.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1//admin/books/")
public class BookAdminRestController {

    private final BookService bookService;

    @Autowired
    public BookAdminRestController(BookService bookAdminService) {
        this.bookService = bookAdminService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addBook(@RequestBody Book book, Principal principal) {
        if (principal != null) {
            bookService.addBook(book);
            return ResponseEntity.ok("Book added successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only administrators can add books.");
        }
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable Long bookId, Principal principal) {
        if (principal != null ) {
            bookService.deleteBook(bookId);
            return ResponseEntity.ok("Book deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only administrators can delete books.");
        }
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportBooksToCsv() {
        // Получаем список всех книг из сервиса
        List<Book> books = bookService.getAllBooks();

        // Преобразуем список книг в CSV формат
        byte[] csvBytes = convertBookListToCsvBytes(books);

        // Определяем HTTP заголовки для возврата CSV файла
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "books.csv");

        // Возвращаем CSV файл в ответе
        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

    private byte[] convertBookListToCsvBytes(List<Book> books) {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Title,Authors,Genres\n");

        for (Book book : books) {
            String authors = book.getAuthor().stream()
                    .map(author -> author.getName())
                    .reduce((a1, a2) -> a1 + ", " + a2)
                    .orElse("");

            String genres = book.getGenre().stream()
                    .map(genre -> genre.getName())
                    .reduce((g1, g2) -> g1 + ", " + g2)
                    .orElse("");

            csvBuilder.append("\"").append(book.getTitle()).append("\",\"")
                    .append(authors).append("\",\"")
                    .append(genres).append("\"\n");
        }

        return csvBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }
}
