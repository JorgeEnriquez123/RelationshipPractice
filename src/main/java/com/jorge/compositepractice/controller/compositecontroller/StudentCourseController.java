package com.jorge.compositepractice.controller.compositecontroller;

import com.jorge.compositepractice.CompositepracticeApplication;
import com.jorge.compositepractice.model.composite.Coursev1;
import com.jorge.compositepractice.model.composite.StudentCoursePK;
import com.jorge.compositepractice.model.composite.Student_course;
import com.jorge.compositepractice.model.composite.Studentv1;
import com.jorge.compositepractice.repository.Coursev1Repository;
import com.jorge.compositepractice.repository.StudentCourseRepository;
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
@RequestMapping("/studentcourse")
public class StudentCourseController {
    private static Logger LOG = LoggerFactory.getLogger(CompositepracticeApplication.class);

    @Autowired
    Studentv1Repository studentv1Repository;
    @Autowired
    Coursev1Repository coursev1Repository;

    @Autowired
    StudentCourseRepository studentCourseRepository;

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(studentCourseRepository.findAll());
    }

    @GetMapping("/paginable")
    public ResponseEntity<?> findAllPaginable(@RequestParam int pageNumber, @RequestParam int pageSize){
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(studentCourseRepository.findAll(page));
    }


    @PostMapping("/student/{studentid}/course/{courseid}")
    public ResponseEntity<?> findById(@PathVariable long studentid, @PathVariable long courseid){
        try {

            LOG.info("TRYING TO FIND THE RELATION");
            StudentCoursePK relationIds = new StudentCoursePK(studentid, courseid);
            Student_course relationFound = studentCourseRepository.findById(relationIds)
                    .orElseThrow(() -> new EntityNotFoundException("Relation not found with IDs: StudentID: "
                            + studentid + " / CourseID: " + courseid));

            studentCourseRepository.save(relationFound);

            LOG.info("RELATION CREATED");
            return ResponseEntity.ok(relationFound);
        }
        catch (EntityNotFoundException e){
            LOG.error("ENTITY NOT FOUND");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRelation(@PathVariable long id){
        Studentv1 stdFound = studentv1Repository.findById(id).orElse(null);
        studentv1Repository.delete(stdFound);
        return ResponseEntity.ok("deleted successfully");
    }
}
