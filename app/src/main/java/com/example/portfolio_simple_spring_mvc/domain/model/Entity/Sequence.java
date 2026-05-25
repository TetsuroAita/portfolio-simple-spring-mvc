package com.example.portfolio_simple_spring_mvc.domain.model.Entity;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

//Entityの新規作成はDBで、既値の更新のみ
@Entity
@Table(name = "sequence")
public class Sequence {

    @Id
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "current_value", nullable = false)
    private Long currentValue;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Deprecated
    protected Sequence() {}

    @PreUpdate
    protected void onUpdated() {
        this.updatedAt = LocalDateTime.now();
    }

    //getter
    public String getName() { return name; }
    public Long getCurrentValue() { return currentValue; }

    //setter
    public void setCurrentValue(Long currentValue) {this.currentValue = Objects.requireNonNull(currentValue); }
}
