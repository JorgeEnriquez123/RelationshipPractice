package com.jorge.compositepractice;

import com.jorge.compositepractice.model.composite.Coursev1;
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

        //studentv1Repository.save(std1);   //SAVING THEM ALREADY [TEST]
        // SAVING THEM ALREADY [NO NEEDED THANKS TO CASCADE - IT WILL STILL WORK THO]

        LOG.info("\n CREATING COURSE 1");
        Coursev1 crs1 = new Coursev1();
        crs1.setName("Math");

        //coursev1Repository.save(crs1);    //SAVING THEM ALREADY [TEST]
        // SAVING THEM ALREADY [NO NEEDED THANKS TO CASCADE - IT WILL STILL WORK THO]

        LOG.info("\n CREATING COURSE 2");
        Coursev1 crs2 = new Coursev1();
        crs2.setName("English");
        coursev1Repository.save(crs2);;     //SAVING COURSE WITHOUT ENROLLING STUDENT

        LOG.info("\n CREATING STUDENT_COURSE (ONE STUDENT AND ONE COURSE)");
        Student_course sc = new Student_course();
        sc.setStudent(std1);
        sc.setCourse(crs1);
        sc.setJoinedDate(LocalDate.now());

        LOG.info("\n SAVING STUDENT");
        studentCourseRepository.save(sc); //IT WILL CREATE THE STUDENT AND THE COURSE

        // DELETING STUDENT
        // studentv1Repository.delete(std1); // YOU CAN'T DUE TO FOREIGN KEY CONSTRAINT

        // -------- TAKING ADVANTAGE OF BIDIRECTIONAL APPROACH --------
    }
}
