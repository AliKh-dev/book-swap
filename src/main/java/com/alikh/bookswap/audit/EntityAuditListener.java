package com.alikh.bookswap.audit;

import com.alikh.bookswap.entity.BaseEntity;
import com.alikh.bookswap.service.Jwt;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EntityAuditListener {

    private String currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            return "Anonymous";
        }
        return ((Jwt)auth.getPrincipal()).getEmail();
    }

    @PrePersist
    public void setCreatedOn(BaseEntity entity) {
        String user = currentUser();
        LocalDateTime now = LocalDateTime.now();

        entity.setCreatedBy(user);
        entity.setCreatedAt(now);

        entity.setUpdatedBy(null);
        entity.setUpdatedAt(null);
    }

    @PreUpdate
    public void setUpdatedOn(BaseEntity entity) {
        String user = currentUser();
        entity.setUpdatedBy(user);
        entity.setUpdatedAt(LocalDateTime.now());
    }
}
