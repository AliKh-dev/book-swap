package com.alikh.bookswap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "role_id")
    private UserRole role;

    @OneToMany(mappedBy = "owner")
    private Set<Book> books;

    @OneToMany(mappedBy = "borrower")
    private Set<BorrowRequest> requests;

    @OneToMany(mappedBy = "resolvedBy")
    private List<Penalty> resolvedPenalties;
}
