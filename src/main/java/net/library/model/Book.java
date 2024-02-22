package net.library.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "book")
@Data
public class Book extends BaseEntity{
    private String title;

    @ManyToMany
    private Set<Author> author;

    @ManyToMany
    private Set<Genre> genre;

}
