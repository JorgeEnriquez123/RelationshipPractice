package com.jorge.compositepractice.repository;

import com.jorge.compositepractice.model.composite.Coursev1;
import com.jorge.compositepractice.model.composite.Studentv1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Coursev1Repository extends JpaRepository<Coursev1, Long> {
    /*@Query("SELECT DISTINCT z FROM Zoo z JOIN FETCH z.bears")
    List<Zoo> findAllWithBears();*/
}
