package net.library.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "author")
@Data
public class Author extends BaseEntity{
    private String name;
}
