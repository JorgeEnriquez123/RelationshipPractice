package com.jorge.compositepractice.repository;

import com.jorge.compositepractice.model.composite.StudentCoursePK;
import com.jorge.compositepractice.model.composite.Student_course;
import com.jorge.compositepractice.model.composite.Studentv1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCourseRepository extends JpaRepository<Student_course, StudentCoursePK> {
    void deleteByStudent(Studentv1 student);
    void deleteByCourse(Studentv1 student);

}
