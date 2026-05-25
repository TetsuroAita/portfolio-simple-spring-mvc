package com.example.portfolio_simple_spring_mvc.domain.model.pagination;

import java.util.List;

public record DomainPagedResult<T>(
    List<T> content,
    int currentPage,
    int totalPages,
    long totalElements,
    boolean hasNext,
    boolean hasPrevious
) {
    public boolean hasContent() {
        return !content.isEmpty();
    }
}
