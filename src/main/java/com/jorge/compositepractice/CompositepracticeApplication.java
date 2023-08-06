package com.jorge.compositepractice;

import com.jorge.compositepractice.model.composite.Coursev1;
import com.jorge.compositepractice.model.composite.StudentCoursePK;
import com.jorge.compositepractice.model.composite.Student_course;
import com.jorge.compositepractice.model.composite.Studentv1;
import com.jorge.compositepractice.repository.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.HashSet;

@SpringBootApplication
public class CompositepracticeApplication implements CommandLineRunner {
    private static Logger LOG = LoggerFactory.getLogger(CompositepracticeApplication.class);
    @Autowired
    Studentv1Repository studentv1Repository;
    @Autowired
    Coursev1Repository coursev1Repository;
    @Autowired
    StudentCourseRepository studentCourseRepository;

    public static void main(String[] args) {
        SpringApplication.run(CompositepracticeApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // ---------
        LOG.info("\n -- COMPOSITE PRACTICE --");
        // ---------

        LOG.info("\n CREATING STUDENT");
        Studentv1 std1 = new Studentv1();
        std1.setName("Jorge");

        Studentv1 studentCreated = studentv1Repository.save(std1);   // * SAVING THEM INDIVIDUALLY

        LOG.info("\n CREATING COURSE 2");
        Coursev1 crs2 = new Coursev1();
        crs2.setName("English");

        Coursev1 courseCreated = coursev1Repository.save(crs2);;     // * SAVING THEM INDIVIDUALLY

        LOG.info("\n SAVING STUDENT");
        StudentCoursePK studentCoursePK = new StudentCoursePK(studentCreated.getId(), courseCreated.getId());
        // * SETTING THE PK CLASS

        Student_course studentCourse = new Student_course();
        studentCourse.setId(studentCoursePK);
        studentCourse.setStudent(studentCreated);
        studentCourse.setCourse(courseCreated);
        studentCourse.setJoinedDate(LocalDate.now());

        studentCourseRepository.save(studentCourse); // * SAVING IT INDIVIDUALLY
    }
}
