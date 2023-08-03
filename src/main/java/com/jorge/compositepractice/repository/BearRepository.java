package com.jorge.compositepractice.repository;

import com.jorge.compositepractice.model.Bear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BearRepository extends JpaRepository<Bear, Long> {
    @Query("SELECT DISTINCT b FROM Bear b JOIN FETCH b.zoo")
    List<Bear> findAllWithZoo();
}
