package com.jorge.compositepractice.controller.compositecontroller;

import com.jorge.compositepractice.CompositepracticeApplication;
import com.jorge.compositepractice.model.composite.Coursev1;
import com.jorge.compositepractice.model.composite.Studentv1;
import com.jorge.compositepractice.repository.Coursev1Repository;
import com.jorge.compositepractice.repository.Studentv1Repository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coursev1")
public class Coursev1Controller {
    private static Logger LOG = LoggerFactory.getLogger(CompositepracticeApplication.class);

    @Autowired
    Studentv1Repository studentv1Repository;
    @Autowired
    Coursev1Repository coursev1Repository;

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(coursev1Repository.findAll());
    }

    @GetMapping("/paginable")
    public ResponseEntity<?> findAllPaginable(@RequestParam int pageNumber, @RequestParam int pageSize){
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(coursev1Repository.findAll(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id){
        return ResponseEntity.ok(coursev1Repository.findById(id).orElse(null));
    }

    @Transactional
    @PostMapping
    public ResponseEntity<?> add(@RequestBody Coursev1 crs){
        return ResponseEntity.ok().body(coursev1Repository.save(crs));
    }


    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable long id){
        Coursev1 courseFound = coursev1Repository.findById(id).orElse(null);
        if(courseFound == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course with ID: " + id + "not Found");
        }
        return ResponseEntity.ok().body("Course with ID: " + id + "successfully deleted");
    }
}
