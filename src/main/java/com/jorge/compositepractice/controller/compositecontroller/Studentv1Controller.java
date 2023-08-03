package com.jorge.compositepractice.controller.compositecontroller;

import com.jorge.compositepractice.CompositepracticeApplication;
import com.jorge.compositepractice.model.composite.Coursev1;
import com.jorge.compositepractice.model.composite.Studentv1;
import com.jorge.compositepractice.repository.Coursev1Repository;
import com.jorge.compositepractice.repository.Studentv1Repository;
import jakarta.persistence.EntityNotFoundException;
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

    @PostMapping("/student/{studentid}/course/{courseid}")
    public ResponseEntity<?> findById(@PathVariable long studentid, @PathVariable long courseid){
        try {
            LOG.info("TRYING TO ENROLL");
            Studentv1 stdfound = studentv1Repository.findById(studentid)
                    .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentid));
            Coursev1 crsfound = coursev1Repository.findById(courseid)
                    .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseid));

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

    /*@GetMapping("/{id}/zoo")
    public ResponseEntity<?> findZooBearIsIn(@PathVariable long id){
        return ResponseEntity.ok(Studentv1Repository.findById(id).get().getZoo());
    }*/
}
