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

        //Studentv1 studentCreated = studentv1Repository.save(std1);   // * SAVING THEM INDIVIDUALLY
                                                                       // NORMALLY, YOU WOULD ASSOCIATE AND EXISTING STUDENT WITH A COURSE
        LOG.info("\n CREATING COURSE 2");
        Coursev1 crs2 = new Coursev1();
        crs2.setName("English");

        //Coursev1 courseCreated = coursev1Repository.save(crs2);;     // * SAVING THEM INDIVIDUALLY
                                                                       // NORMALLY, YOU WOULD ASSOCIATE AND EXISTING COURSE WITH A STUDENT
        //LOG.info("\n SAVING STUDENT");
        StudentCoursePK studentCoursePK = new StudentCoursePK(std1.getId(), crs2.getId());
        // SETTING THE PK CLASS

        Student_course studentCourse = new Student_course();
        studentCourse.setStudent(std1);
        studentCourse.setCourse(crs2);
        studentCourse.setId(studentCoursePK);
        studentCourse.setJoinedDate(LocalDate.now());

        studentCourseRepository.save(studentCourse);    // * PERSISTING ALONG WITH STUDENT AND COURSE

        // ! MY MISTAKE - WHY DIDN'T EMBEDDEDID WORK FOR PERSISTING CHILD ENTITES ALONG WITH A PARENT ENTITY (COMPOSITE ONE):
        // - HAD 'long' instead of 'Long' on StudentCoursePK Ids, which won't let us store a null for ID (From an non-persisted Entity)
        // - FORGOT TO ADD CASCADE PERSIST AND MERGE
        // - WRONG NAME IN @MAPSID

    }
}
