package com.jorge.compositepractice.repository;

import com.jorge.compositepractice.model.Zoo;
import com.jorge.compositepractice.model.composite.Studentv1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Studentv1Repository extends JpaRepository<Studentv1, Long> {
    /*@Query("SELECT DISTINCT z FROM Zoo z JOIN FETCH z.bears")
    List<Zoo> findAllWithBears();*/
}
