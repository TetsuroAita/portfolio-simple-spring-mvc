package com.example.portfolio_simple_spring_mvc.domain.model.Entity;

import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "avatar")
public class Avatar extends BaseEntityModel {

    @Column(name = "active", nullable = false)
    private boolean active = false;

    @Column(name = "original_filename", nullable = false, length = 200)
    private String originalFilename;
    
    @Column(name = "content_type", nullable = false, length = 30)
    private String contentType;
    
    @Column(name = "content_size", nullable = false)
    private long contentSize;
    
    @Column(name = "changed_stem", nullable = false, unique = true, length = 200)
    private String changedStem = UUID.randomUUID().toString();

    @Deprecated
    protected Avatar() {}

    public Avatar(
        String originalFilename,
        String contentType,
        long contentSize
    ) {
        super();
        this.originalFilename = Objects.requireNonNull(originalFilename);
        this.contentType = Objects.requireNonNull(contentType);
        this.contentSize = Objects.requireNonNull(contentSize);
    }

    // getter
    public boolean activeStatus() { return active; }
    public String getOriginalFilename() { return this.originalFilename; }
    public String getContentType() { return this.contentType; }
    public long getContentSize() { return this.contentSize; }
    public String getChangedStem() { return this.changedStem; }

    // setter
    public void setOriginalFilename(String originalFilename) { this.originalFilename = Objects.requireNonNull(originalFilename); }
    public void setContentType(String contentType) { this.contentType = Objects.requireNonNull(contentType); }
    public void setContentSize(long contentSize) { this.contentSize = Objects.requireNonNull(contentSize); }

    public Avatar activate() {
        this.active = true;
        return this;
    }

    public Avatar inactivate() {
        this.active = false;
        return this;
    }
    
    // ストレージへアクセスする際のパス発行
    public String generatePath() {
        String extention = contentType.substring(contentType.lastIndexOf("/") + 1);
        String accessPath = changedStem + "." + extention;
        return accessPath;
    }

    // テスト用ファクトリメソッド
    public static Avatar createObjectForTest() {
        return new Avatar(
            "test.png",
            "image/png",
            1L
        );
    }
}
