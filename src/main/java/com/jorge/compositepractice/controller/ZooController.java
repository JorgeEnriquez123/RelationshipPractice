package com.jorge.compositepractice.controller;

import com.jorge.compositepractice.CompositepracticeApplication;
import com.jorge.compositepractice.model.Bear;
import com.jorge.compositepractice.model.Zoo;
import com.jorge.compositepractice.repository.BearRepository;
import com.jorge.compositepractice.repository.ZooRepository;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zoo")
public class ZooController {
    private static Logger LOG = LoggerFactory.getLogger(ZooController.class);
    @Autowired
    ZooRepository zooRepository;
    @Autowired
    BearRepository bearRepository;

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(zooRepository.findAll());
    }
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Zoo zoo){
        return ResponseEntity.ok(zooRepository.save(zoo));
    }

    @GetMapping("/paginable")
    public ResponseEntity<?> findAllPaginable(@RequestParam int pageNumber, @RequestParam int pageSize){
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(zooRepository.findAll(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id){
        return ResponseEntity.ok(zooRepository.findById(id).orElse(null));
    }

    @GetMapping("/{id}/bears")
    public ResponseEntity<?> findBears(@PathVariable long id){
        return ResponseEntity.ok(zooRepository.findById(id).get().getBears());
    }
}
