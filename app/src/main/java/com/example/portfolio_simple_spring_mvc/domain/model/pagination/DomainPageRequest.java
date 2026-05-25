package com.example.portfolio_simple_spring_mvc.domain.model.pagination;

public record DomainPageRequest(
    int page,
    int size,
    String sortBy,
    boolean asc
) {
    public int getOffset() {
        return page * size;
    }
}
