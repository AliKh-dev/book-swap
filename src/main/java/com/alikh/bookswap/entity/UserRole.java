package com.alikh.bookswap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "user_role")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole extends BaseEntity {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "code")
    private String code;

    @OneToMany(mappedBy = "role")
    private Set<AppUser> users;
}
