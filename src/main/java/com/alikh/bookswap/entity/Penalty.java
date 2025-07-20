package com.alikh.bookswap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "penalty")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Penalty extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "reason")
    private String reason;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;


    // Relationships
    @OneToOne
    @JoinColumn(name = "request_id")
    private BorrowRequest request;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private PenaltyType type;

    @ManyToOne
    @JoinColumn(name = "resolved_by_id")
    private AppUser resolvedBy;
}