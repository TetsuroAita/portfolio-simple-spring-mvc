package com.example.portfolio_simple_spring_mvc.infrastructure.persistence.jpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Sequence;

import jakarta.persistence.LockModeType;

@Repository
public interface SequenceRepository extends JpaRepository<Sequence, String> {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE) //悲観ロック（Pessimistic Lock）
    @Query("SELECT s FROM Sequence s WHERE s.name = :name")
    Sequence findByNameForUpdate(String name);
}
