package com.example.portfolio_simple_spring_mvc.infrastructure.persistence.jpaRepository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    // Active状態かつ指定のカラム名でソート
    Page<Profile> findByActiveFalse(Pageable pageable);

    // 非Actrive状態かつ指定のカラム名でソート
    Page<Profile> findByActiveTrue(Pageable pageable);
}
