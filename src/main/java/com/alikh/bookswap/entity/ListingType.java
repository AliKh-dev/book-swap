package com.alikh.bookswap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "listing_type")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingType extends BaseEntity {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "code")
    private String code;


    // Relationships
    @OneToMany(mappedBy = "type")
    private Set<Listing> listings;
}
