package com.jorge.compositepractice.controller.compositecontroller;

import com.jorge.compositepractice.CompositepracticeApplication;
import com.jorge.compositepractice.model.composite.Coursev1;
import com.jorge.compositepractice.model.composite.StudentCoursePK;
import com.jorge.compositepractice.model.composite.Student_course;
import com.jorge.compositepractice.model.composite.Studentv1;
import com.jorge.compositepractice.model.request.CourseRequest;
import com.jorge.compositepractice.model.request.StudentRequest;
import com.jorge.compositepractice.repository.Coursev1Repository;
import com.jorge.compositepractice.repository.StudentCourseRepository;
import com.jorge.compositepractice.repository.Studentv1Repository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/studentv1")
public class Studentv1Controller {
    private static Logger LOG = LoggerFactory.getLogger(CompositepracticeApplication.class);

    @Autowired
    Studentv1Repository studentv1Repository;
    @Autowired
    Coursev1Repository coursev1Repository;
    @Autowired
    StudentCourseRepository studentCourseRepository;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(studentv1Repository.findAll());
    }

    @GetMapping("/paginable")
    public ResponseEntity<?> findAllPaginable(@RequestParam int pageNumber, @RequestParam int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(studentv1Repository.findAll(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        return ResponseEntity.ok(studentv1Repository.findById(id).orElse(null));
    }

    @Transactional
    @PostMapping
    public ResponseEntity<?> add(@RequestBody Studentv1 stdnt) {  // * SAVE - FOR @ManyToMany
        return ResponseEntity.ok().body(studentv1Repository.save(stdnt));
    }

    @Transactional
    @PostMapping("/idclassver") // * SAVE - ESPECIALLY FOR IdClass VERSION
    public ResponseEntity<?> addWithCourses(@RequestBody StudentRequest stdnt) {
        LocalDate localDate = LocalDate.now();
        Studentv1 stdToAdd = new Studentv1();
        stdToAdd.setName(stdnt.getName());

        if(!stdnt.getCourses().isEmpty()) {
            for (CourseRequest crs : stdnt.getCourses()) {
                Coursev1 courseToAdd = new Coursev1();
                courseToAdd.setName(crs.getName());

                Student_course stdcrsRelation = new Student_course(stdToAdd, courseToAdd, localDate);
                stdToAdd.getStudentCourses().add(stdcrsRelation);
            }
            studentv1Repository.save(stdToAdd);
            return ResponseEntity.ok().body("Student with courses saved successfully");
        }
        studentv1Repository.save(stdToAdd);
        return ResponseEntity.ok().body("Student saved successfully");
    }

    @Transactional
    @PostMapping("/{studentid}/course/{courseid}")
    public ResponseEntity<?> assignCourse(@PathVariable long studentid, @PathVariable long courseid) {
        LOG.info("SEARCHING STUDENT AND COURSE");
        Studentv1 student = studentv1Repository.findById(studentid).orElse(null);
        Coursev1 course = coursev1Repository.findById(courseid).orElse(null);

        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with ID: " + studentid + " doesn't exist");
        }
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course with ID: " + courseid + " doesn't exist");
        }

        Set<Student_course> studentsFromCourse = course.getStudentCourses();
        LOG.info("GETTING RELATION OF STUDENTS FROM THE COURSE");

        if (!studentsFromCourse.isEmpty()) {
            boolean studentIsEnrolled = studentsFromCourse.stream()
                    .filter(stdCrsRelation -> stdCrsRelation.getStudent() == student)
                    .map(Student_course::getCourse)
                    .anyMatch(coursev1 -> coursev1.getId().equals(courseid));

            if (studentIsEnrolled) {
                LOG.info("ALREADY ENROLLED");
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Student with ID: " + studentid + " already enrolled on course ID: " + courseid);
            }
        }
        Student_course studentCourseRelationToAdd = new Student_course(student, course, LocalDate.now());
        student.getStudentCourses().add(studentCourseRelationToAdd); // ADDING RELATIONSHIP TO STUDENT
        studentv1Repository.save(student); // SAVING STUDENT WITH THE RELATIONSHIP
        return ResponseEntity.ok().body("Student with ID: " + studentid + " enrolled in course ID: " + courseid);
    }

    @Transactional
    @DeleteMapping("/{studentid}/remove/course/{courseid}")
    public ResponseEntity<?> removeCourse(@PathVariable long studentid, @PathVariable long courseid) {
        LOG.info("SEARCHING STUDENT AND COURSE");
        Studentv1 student = studentv1Repository.findById(studentid).orElse(null);
        Coursev1 course = coursev1Repository.findById(courseid).orElse(null);

        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with ID: " + studentid + " doesn't exist");
        }
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course with ID: " + courseid + " doesn't exist");
        }

        Set<Student_course> coursesFromStudent = student.getStudentCourses();
        LOG.info("GETTING RELATION OF STUDENTS FROM THE COURSE");

        if (!coursesFromStudent.isEmpty()) {
            boolean studentIsEnrolled = coursesFromStudent.stream()
                    .filter(stdCrsRelation -> stdCrsRelation.getStudent() == student)
                    .map(Student_course::getCourse)
                    .anyMatch(coursev1 -> coursev1.getId().equals(courseid));

            if (studentIsEnrolled) {
                LOG.info("STUDENT ENROLLED");
                StudentCoursePK studentCoursePK = new StudentCoursePK(student.getId(), course.getId());
                Student_course studentCourse = studentCourseRepository.findById(studentCoursePK).get();
                studentCourseRepository.deleteById(studentCoursePK); //REMOVE RELATIONSHIP FROM COMPOSITE TABLE
                student.getStudentCourses().remove(studentCourse); //REMOVE RELATIONSHIP FROM BASE ENTITY
                course.getStudentCourses().remove(studentCourse);
                studentv1Repository.save(student);  //SAVING WITH UPDATED RELATIONSHIP
                return ResponseEntity.ok().body("Student with ID: " + studentid + " removed from Course ID: " + courseid);
            }
            else {
                return ResponseEntity.ok().body("Student with ID: " + studentid + " is not enrolled in course ID: " + courseid);
            }
        }
        return ResponseEntity.ok().body("Student with ID: " + studentid + " doesn't have any courses");
    }

    @Transactional
    @PutMapping("/update/{studentid}")
    public ResponseEntity<?> updateStudent(@PathVariable long studentid, @RequestBody Studentv1 std) {
        Optional<Studentv1> optionalStudent = studentv1Repository.findById(studentid);
        if (optionalStudent.isPresent()) {
            Studentv1 stdUpdated = optionalStudent.get();
            stdUpdated.setName(std.getName());
            return ResponseEntity.ok().body(studentv1Repository.save(stdUpdated));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with ID: [" + studentid + "] doesn't exist");
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable long id) {
        Studentv1 studentFound = studentv1Repository.findById(id).orElse(null);
        if (studentFound == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with ID: " + id + " not Found");
        }
        studentCourseRepository.deleteByStudent(studentFound);
        studentv1Repository.delete(studentFound);
        return ResponseEntity.ok().body("Student with ID: " + id + " sucessfully deleted");
    }
}
