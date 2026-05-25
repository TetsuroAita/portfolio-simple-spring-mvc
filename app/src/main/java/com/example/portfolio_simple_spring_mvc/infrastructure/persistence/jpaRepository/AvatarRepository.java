package com.example.portfolio_simple_spring_mvc.infrastructure.persistence.jpaRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;

public interface AvatarRepository extends JpaRepository<Avatar, UUID> {
    // 非アクティブ状態の avatar を一覧で取得
    List<Avatar> findByActiveFalse();

    // 非アクティブ状態の avatar を削除
    long deleteByActiveFalse();
}
