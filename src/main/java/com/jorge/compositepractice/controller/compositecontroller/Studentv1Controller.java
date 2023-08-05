package com.jorge.compositepractice.controller.compositecontroller;

import com.jorge.compositepractice.CompositepracticeApplication;
import com.jorge.compositepractice.model.Bear;
import com.jorge.compositepractice.model.Zoo;
import com.jorge.compositepractice.model.composite.Coursev1;
import com.jorge.compositepractice.model.composite.Studentv1;
import com.jorge.compositepractice.repository.Coursev1Repository;
import com.jorge.compositepractice.repository.Studentv1Repository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/studentv1")
public class Studentv1Controller {
    private static Logger LOG = LoggerFactory.getLogger(CompositepracticeApplication.class);

    @Autowired
    Studentv1Repository studentv1Repository;
    @Autowired
    Coursev1Repository coursev1Repository;

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(studentv1Repository.findAll());
    }

    @GetMapping("/paginable")
    public ResponseEntity<?> findAllPaginable(@RequestParam int pageNumber, @RequestParam int pageSize){
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(studentv1Repository.findAll(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id){
        return ResponseEntity.ok(studentv1Repository.findById(id).orElse(null));
    }

    @Transactional
    @PostMapping
    public ResponseEntity<?> add(@RequestBody Studentv1 stdnt){
        return ResponseEntity.ok().body(studentv1Repository.save(stdnt));
    }

    @Transactional
    @PostMapping("/{studentid}/courses")
    public ResponseEntity<?> assignManyCourses(@PathVariable long studentid, @RequestBody List<Long> coursesId){
        LOG.info("SEARCHING STUDENT");
        Studentv1 student = studentv1Repository.findById(studentid).orElse(null);
        if (student == null) {
            LOG.error("STUDENT IS NULL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with ID: " + studentid + " doesn't exist");
        }
        if (coursesId.isEmpty()){
            LOG.error("NO COURSES ID GIVEN");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No ID(s) were given");
        }
        LOG.info("[ -- STUDENT AND COURSES VALID -- ]");
        Map<Long, String> result = new HashMap<>();
        LOG.info("ITERATING IDS");
        for(Long ids : coursesId){
            LOG.info("TRYING TO FIND COURSES BY IDS");
            LOG.info("ID ITERATED FROM ARRAY: " + ids);

            Coursev1 courseFound = coursev1Repository.findById(ids).orElse(null);
            if(courseFound == null){
                LOG.info("COURSE NOT FOUND BY ID ITERATED");
                result.put(ids, "Course with ID: " + ids + " doesn't exist");
                continue;
            }
            LOG.info("COURSE FOUND");

            LOG.info("ITERATING THROUGH COURSES FROM STUDENT TO CHECK IF IT WAS ALREADY ADDED");
            Set<Coursev1> coursesFromTheStudent = student.getCourses();
            boolean toAdd = true;
            for(Coursev1 courseFromStudent : coursesFromTheStudent){
                LOG.info("COMPARING COURSEFOUND WITH COURSES FROM STUDENT");
                LOG.info("COMPARING IT WITH COURSE ID: " + courseFromStudent.getId() + " (STUDENT COURSE)");
                if(courseFound.getId().equals(courseFromStudent.getId())){
                    LOG.info("THE COURSE FOUND BY THE ITERATED ID IS EQUAL TO THE STUDENT COURSE");
                    LOG.info("--- COURSE WON'T BE ADDED ---");
                    toAdd = false;
                    break;
                }
                else {
                    LOG.info("THE COURSE FOUND BY THE ITERATED ID IS NOT EQUAL TO THE STUDENT COURSE");
                }
            }
            if(toAdd){
                LOG.info("COMPARISON FINISHED : ACTION: -- ADDING COURSE WITH ID: " + courseFound.getId() + " --");
                student.getCourses().add(courseFound);
                result.put(ids, "Course with ID: " + ids + " successfully added");
            }
            else{
                LOG.info("COMPARISON FINISHED : ACTION: -- NOT ADDING COURSE WITH ID: " + courseFound.getId() + " --");
                result.put(ids, "Course with ID: " + ids + " already set on Student");
            }
        }
        LOG.info("-- ENDING ITERATION --");
        LOG.info("-- SAVING STUDENT WITH SET COURSES --");
        studentv1Repository.save(student);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PostMapping("/{studentid}/course/{courseid}")
    public ResponseEntity<?> assignCourse(@PathVariable long studentid, @PathVariable long courseid){
        LOG.info("SEARCHING STUDENT AND COURSE");
        Studentv1 student = studentv1Repository.findById(studentid).orElse(null);
        Coursev1 course = coursev1Repository.findById(courseid).orElse(null);

        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with ID: " + studentid + " doesn't exist");
        }
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course with ID: " + courseid + " doesn't exist");
        }

        Set<Studentv1> studentsFromCourse = course.getStudents();
        if (studentsFromCourse != null) {
            for(Studentv1 stdsInCourse : studentsFromCourse){
                if(student.getId().equals(stdsInCourse.getId())){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Student with ID: " + studentid + " already enrolled on course ID: " + courseid);
                }
            }
        }
        student.getCourses().add(course);
        studentv1Repository.save(student);
        return ResponseEntity.ok().body("Student with ID: " + studentid + " enrolled in course ID: " + courseid);
    }

    @Transactional
    @PutMapping("/update/{studentid}")
    public ResponseEntity<?> updateStudent(@PathVariable long studentid, @RequestBody Studentv1 std) {
        Optional<Studentv1> optionalStudent = studentv1Repository.findById(studentid);
        if(optionalStudent.isPresent()){
            Studentv1 stdUpdated = optionalStudent.get();
            stdUpdated.setName(std.getName());
            return ResponseEntity.ok().body(studentv1Repository.save(stdUpdated));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with ID: [" + studentid + "] doesn't exist");
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable long id){
        Studentv1 studentFound = studentv1Repository.findById(id).orElse(null);
        if(studentFound == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with ID: " + id + " not Found");
        }
        studentv1Repository.delete(studentFound);
        return ResponseEntity.ok().body("Student with ID: " + id + " sucessfully deleted");
    }
}
