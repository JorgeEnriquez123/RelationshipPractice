package com.jorge.compositepractice.controller;

import com.jorge.compositepractice.CompositepracticeApplication;
import com.jorge.compositepractice.model.Bear;
import com.jorge.compositepractice.model.Zoo;
import com.jorge.compositepractice.repository.BearRepository;
import com.jorge.compositepractice.repository.ZooRepository;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/zoo")
public class ZooController {
    private static Logger LOG = LoggerFactory.getLogger(ZooController.class);
    @Autowired
    ZooRepository zooRepository;
    @Autowired
    BearRepository bearRepository;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(zooRepository.findAll());
    }

    @GetMapping("/paginable")
    public ResponseEntity<?> findAllPaginable(@RequestParam int pageNumber, @RequestParam int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(zooRepository.findAll(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        return ResponseEntity.ok(zooRepository.findById(id).orElse(null));
    }

    @Transactional
    @PostMapping
    public ResponseEntity<?> addZoo(@RequestBody Zoo zoo) {
        return ResponseEntity.ok().body(zooRepository.save(zoo));
    }

    @Transactional
    @PostMapping("/{zooid}/bear/{bearid}")
    public ResponseEntity<?> assignBear(@PathVariable long zooid, @PathVariable long bearid) {
        LOG.info("SEARCHING ZOO AND BEAR");
        Zoo zoo = zooRepository.findById(zooid).orElse(null);
        Bear bear = bearRepository.findById(bearid).orElse(null);

        if (zoo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zoo with ID: " + zooid + " doesn't exist");
        }
        if (bear == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bear with ID: " + bearid + " doesn't exist");
        }

        if (bear.getZoo() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Bear with ID: " + bearid + " already set on another Zoo");
        }
        bear.setZoo(zoo); // ? IMPORTANT TO SET THE RELATIONSHIP !!!
        zoo.getBears().add(bear) ;

        zooRepository.save(zoo);
        return ResponseEntity.ok().body("Bear with ID: " + bear.getId() + " added successfully");
    }

    @Transactional
    @PutMapping("/update/{zooid}")
    public ResponseEntity<?> updateZoo(@PathVariable long zooid, @RequestBody Zoo zoo) {
        Optional<Zoo> optionalZoo = zooRepository.findById(zooid);
        if(optionalZoo.isPresent()){
            Zoo zooUpdated = optionalZoo.get();
            zooUpdated.setName(zoo.getName());
            zooUpdated.setAddress(zoo.getAddress());
            return ResponseEntity.ok().body(zooRepository.save(zooUpdated));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zoo with ID: " + zooid + " doesn't exist");
    }

    @Transactional
    @DeleteMapping("/delete/{zooid}")
    public ResponseEntity<?> deleteZoo(@PathVariable long zooid){
        Optional<Zoo> optionalZoo = zooRepository.findById(zooid);
        if(optionalZoo.isPresent()){
            zooRepository.delete(optionalZoo.get());
            return ResponseEntity.ok().body("Bear with ID: " + zooid + " successfully deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bear with ID: " + zooid + " doesn't exist");
    }
}
