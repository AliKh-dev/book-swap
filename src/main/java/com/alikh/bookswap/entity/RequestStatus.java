package com.alikh.bookswap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "request_status")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestStatus extends BaseEntity {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "code")
    private String code;

    // Relationships
    @OneToMany(mappedBy = "status")
    private Set<BorrowRequest> requests;
}
