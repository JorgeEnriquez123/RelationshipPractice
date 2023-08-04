package com.jorge.compositepractice.controller.compositecontroller;

import com.jorge.compositepractice.CompositepracticeApplication;
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
    @PostMapping("/student/{studentid}/course/{courseid}")
    public ResponseEntity<?> findById(@PathVariable long studentid, @PathVariable long courseid){
        try {
            LOG.info("TRYING TO ENROLL");
            Studentv1 stdfound = studentv1Repository.findById(studentid)
                    .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentid));
            Coursev1 crsfound = coursev1Repository.findById(courseid)
                    .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseid));

            if(stdfound.getCourses().stream().anyMatch(coursev1 -> coursev1.getId().equals(crsfound.getId()))){
                LOG.error("STUDENT ALREADY ENROLLED");
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Student already enrolled in that course");
            }
            stdfound.getCourses().add(crsfound);
            studentv1Repository.save(stdfound);

            LOG.info("COURSE ASSIGNED TO THE STUDENT");
            return ResponseEntity.ok(stdfound);
        }
        catch (EntityNotFoundException e){
            LOG.error("ENTITY NOT FOUND");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //@Transactional
    @PostMapping
    public ResponseEntity<?> addStudentWithCourses(@RequestBody Studentv1 stdnt){
        return ResponseEntity.ok().body(studentv1Repository.save(stdnt));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable long id){
        Studentv1 studentFound = studentv1Repository.findById(id).orElse(null);
        if(studentFound == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with ID: " + id + "not Found");
        }
        return ResponseEntity.ok().body("Student with ID: " + id + "sucessfully deleted");
    }
}
