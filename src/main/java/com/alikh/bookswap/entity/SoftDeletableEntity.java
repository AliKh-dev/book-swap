package com.alikh.bookswap.entity;

import com.alikh.bookswap.audit.EntityAuditListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(EntityAuditListener.class)
public abstract class SoftDeletableEntity extends BaseEntity {

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by", insertable = false)
    private String deletedBy;
}
