package com.example.portfolio_simple_spring_mvc.domain.model.Entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@MappedSuperclass
public abstract class BaseEntityModel {

    @Id //フィールドアクセスモード(対義語:プロパティアクセスモード)
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id = UUID.randomUUID();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    //リフレクション、サブクラス用
    protected BaseEntityModel() {}

    //永続化時のみ発火(nullチェックで新規か更新か判断)
    @PrePersist
    public void onCreated() {
        if(this.createdAt == null) this.createdAt = LocalDateTime.now();
        if(this.updatedAt == null) this.updatedAt = this.createdAt;
    }
    
    //更新時のみ発火
    @PreUpdate
    public void onUpdated() {
        this.updatedAt = LocalDateTime.now();
    }
    
    //getter
    public UUID getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof BaseEntityModel)) return false;
        BaseEntityModel other = (BaseEntityModel)obj;
        return id != null
            && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(54);
    }
}
