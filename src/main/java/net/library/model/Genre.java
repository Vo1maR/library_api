package net.library.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "genre")
@Data
public class Genre extends BaseEntity{
    private String name;
}
