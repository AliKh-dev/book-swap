package com.alikh.bookswap.audit;

import com.alikh.bookswap.entity.BaseEntity;
import com.alikh.bookswap.entity.SoftDeletableEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EntityAuditListener {

    private String currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "Anonymous";
        }
        return auth.getName();
    }

    @PrePersist
    public void setCreatedOn(BaseEntity entity) {
        String user = currentUser();
        LocalDateTime now = LocalDateTime.now();

        entity.setCreatedBy(user);
        entity.setCreatedAt(now);

        entity.setUpdatedBy(user);
        entity.setUpdatedAt(now);
    }

    @PreUpdate
    public void setUpdatedOn(BaseEntity entity) {
        String user = currentUser();
        entity.setUpdatedBy(user);
        entity.setUpdatedAt(LocalDateTime.now());
    }

    @PreRemove
    public void onSoftDelete(SoftDeletableEntity entity) {
        entity.setIsDeleted(true);
        entity.setDeletedBy(currentUser());
        entity.setDeletedAt(LocalDateTime.now());
    }
}
