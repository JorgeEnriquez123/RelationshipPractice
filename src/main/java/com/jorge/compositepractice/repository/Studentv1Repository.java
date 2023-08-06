package com.jorge.compositepractice.repository;

import com.jorge.compositepractice.model.composite.Studentv1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Studentv1Repository extends JpaRepository<Studentv1, Long> {
}
