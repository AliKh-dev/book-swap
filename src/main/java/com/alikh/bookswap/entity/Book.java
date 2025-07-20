package com.alikh.bookswap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "BOOK")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "genre")
    private String genre;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "description")
    private String description;


    // Relationships
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private AppUser owner;

    @ManyToOne
    @JoinColumn(name = "condition_id")
    private BookCondition condition;

    @OneToMany(mappedBy = "book")
    private Set<Listing> listings;
}