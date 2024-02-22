package net.library.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import net.library.model.Book;
import net.library.model.Status;
import net.library.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminUserDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String status;
    private List<Long> favoriteBookIds; // Список идентификаторов любимых книг


    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setStatus(Status.valueOf(status));

        // Добавляем любимые книги
        if (favoriteBookIds != null) {
            List<Book> favoriteBooks = favoriteBookIds.stream()
                    .map(bookId -> {
                        Book book = new Book();
                        book.setId(bookId);
                        return book;
                    })
                    .collect(Collectors.toList());
            user.setFavoriteBooks(favoriteBooks);
        }

        return user;
    }

    public static AdminUserDto fromUser(User user) {
        AdminUserDto adminUserDto = new AdminUserDto();
        adminUserDto.setId(user.getId());
        adminUserDto.setUsername(user.getUsername());
        adminUserDto.setFirstName(user.getFirstName());
        adminUserDto.setLastName(user.getLastName());
        adminUserDto.setEmail(user.getEmail());
        adminUserDto.setStatus(user.getStatus().name());

        // Получаем идентификаторы любимых книг пользователя
        if (user.getFavoriteBooks() != null) {
            List<Long> favoriteBookIds = user.getFavoriteBooks().stream()
                    .map(Book::getId)
                    .collect(Collectors.toList());
            adminUserDto.setFavoriteBookIds(favoriteBookIds);
        }

        return adminUserDto;
    }
}
