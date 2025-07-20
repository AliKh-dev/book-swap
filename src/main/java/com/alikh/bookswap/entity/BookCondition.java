package com.alikh.bookswap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "book_condition")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCondition extends BaseEntity {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "code")
    private String code;

    // Relationships
    @OneToMany(mappedBy = "condition")
    private Set<Book> books;
}
