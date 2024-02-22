package net.library.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import net.library.model.Book;
import net.library.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private List<Long> favoriteBookIds; // Список идентификаторов любимых книг

    public User toUser(){
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
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

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());

        // Получаем идентификаторы любимых книг пользователя
        if (user.getFavoriteBooks() != null) {
            List<Long> favoriteBookIds = user.getFavoriteBooks().stream()
                    .map(Book::getId)
                    .collect(Collectors.toList());
            userDto.setFavoriteBookIds(favoriteBookIds);
        }

        return userDto;
    }
}
