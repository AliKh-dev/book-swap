package com.alikh.bookswap.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "listing")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Listing extends SoftDeletableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "rental_days")
    private Integer rentalDays;

    @Column(name = "is_active")
    private Boolean isActive = true;


    // Virtual Column
    @Column(name = "active_book_id", insertable = false, updatable = false)
    private Long activeBookId;


    // Relationships
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private ListingType type;

    @OneToMany(mappedBy = "listing")
    private Set<BorrowRequest> requests;
}