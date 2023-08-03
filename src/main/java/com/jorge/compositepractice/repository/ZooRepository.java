package com.jorge.compositepractice.repository;

import com.jorge.compositepractice.model.Bear;
import com.jorge.compositepractice.model.Zoo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZooRepository extends JpaRepository<Zoo, Long> {
    @Query("SELECT DISTINCT z FROM Zoo z JOIN FETCH z.bears")
    List<Zoo> findAllWithBears();
}
