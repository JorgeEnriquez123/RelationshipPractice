package com.jorge.compositepractice.controller;

import com.jorge.compositepractice.repository.BearRepository;
import com.jorge.compositepractice.repository.ZooRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zoo")
public class ZooController {
    @Autowired
    ZooRepository zooRepository;

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(zooRepository.findAll());
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
