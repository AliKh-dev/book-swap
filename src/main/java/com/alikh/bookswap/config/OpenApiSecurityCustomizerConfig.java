package com.alikh.bookswap.config;

import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

import java.util.Collections;
import java.util.List;

@Configuration
public class OpenApiSecurityCustomizerConfig {

    private static final String SCHEME_NAME = "bearerAuth";
    private final AntPathMatcher matcher = new AntPathMatcher();

    private static final List<String> PUBLIC_ALL_METHODS = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "/api/auth/refresh"
    );

    private static final List<String> PUBLIC_GET_ONLY = List.of(
            "/api/books/**"
    );

    private boolean matchesAny(String path, List<String> patterns) {
        for (var p : patterns) {
            if (matcher.match(p, path)) return true;
        }
        return false;
    }

    private boolean isPublic(String path, PathItem.HttpMethod method) {
        if (matchesAny(path, PUBLIC_ALL_METHODS))
            return true;
        return method == PathItem.HttpMethod.GET && matchesAny(path, PUBLIC_GET_ONLY);
    }

    @Bean
    public OpenApiCustomizer applyJwtRequirement() {
        return openApi -> {
            if (openApi.getPaths() == null) return;

            openApi.getPaths().forEach((path, pathItem) -> {
                if (pathItem.readOperationsMap() == null) return;

                pathItem.readOperationsMap().forEach((httpMethod, operation) -> {
                    if (isPublic(path, httpMethod)) {
                        operation.setSecurity(Collections.emptyList());
                    } else {
                        var req = new SecurityRequirement().addList(SCHEME_NAME);
                        var existing = operation.getSecurity();
                        if (existing == null || existing.isEmpty()) {
                            operation.setSecurity(List.of(req));
                        } else if (existing.stream().noneMatch(s -> s.containsKey(SCHEME_NAME))) {
                            existing.add(req);
                        }
                    }
                });
            });
        };
    }
}
