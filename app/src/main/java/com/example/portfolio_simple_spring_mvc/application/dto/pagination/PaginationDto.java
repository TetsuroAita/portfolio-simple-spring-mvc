package com.example.portfolio_simple_spring_mvc.application.dto.pagination;

import java.util.List;

public record PaginationDto<T>(
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
