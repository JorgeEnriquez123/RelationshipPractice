package com.jorge.compositepractice.controller;

import com.jorge.compositepractice.model.Bear;
import com.jorge.compositepractice.model.DTO.BearDTO;
import com.jorge.compositepractice.model.Zoo;
import com.jorge.compositepractice.repository.BearRepository;
import com.jorge.compositepractice.repository.ZooRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bear")
public class BearController {
    @Autowired
    BearRepository bearRepository;
    @Autowired
    ZooRepository zooRepository;

    @GetMapping
    public ResponseEntity<?> findAll(){
        List<Bear> bearList = bearRepository.findAllWithZoo();
        List<BearDTO> bearDTOList = new ArrayList<>();

        for(Bear b : bearList){
            // Create DTO with desired attributes
            BearDTO bearDTO = new BearDTO();
            // Mapping DTO
            bearDTO.setId(b.getId());
            bearDTO.setName(b.getName());
            bearDTO.setSpecies(b.getSpecies());
            if(b.getZoo() == null){
                bearDTO.setZooname(null);
            }
            else {
                bearDTO.setZooname(b.getZoo().getName());
            }
            //Adding it to the List
            bearDTOList.add(bearDTO);
        }
        return ResponseEntity.ok(bearDTOList);
        //Returning it
    }

    @GetMapping("/paginable")
    public ResponseEntity<?> findAllPaginable(@RequestParam int pageNumber, @RequestParam int pageSize){
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(bearRepository.findAll(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id){
        return ResponseEntity.ok(bearRepository.findById(id).orElse(null));
    }

    @Transactional
    @PostMapping()
    public ResponseEntity<?> addBear(@RequestBody Bear bear){
        return ResponseEntity.ok().body(bearRepository.save(bear));
    }

    @Transactional
    @PostMapping("/{bearid}/zoo")
    public ResponseEntity<?> assignZoo(@PathVariable long bearid, @RequestBody Zoo zoo){
        Bear bear = bearRepository.findById(bearid).orElse(null);
        if(bear == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bear with ID: " + bearid + " doesn't exist");
        }
        Zoo zooToAdd = zooRepository.findById(zoo.getId()).orElse(null);
        if(zooToAdd == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zoo with ID: " + zoo.getId() + " Doesn't exist");
        }
        bear.setZoo(zooToAdd);
        return ResponseEntity.ok().body(bearRepository.save(bear));

    }

    @Transactional
    @PutMapping("/update/{bearid}")
    public ResponseEntity<?> updateBear(@PathVariable long bearid, @RequestBody Bear bear) {
        Optional<Bear> optionalBear = bearRepository.findById(bearid);
        if(optionalBear.isPresent()){
            Bear bearUpdated = optionalBear.get();
            bearUpdated.setName(bear.getName());
            bearUpdated.setSpecies(bear.getSpecies());
            return ResponseEntity.ok().body(bearRepository.save(bearUpdated));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bear with ID: " + bearid + " doesn't exist");
    }

    @Transactional
    @DeleteMapping("/delete/{bearid}")
    public ResponseEntity<?> deleteBear(@PathVariable long bearid){
        Optional<Bear> optionalBear = bearRepository.findById(bearid);
        if(optionalBear.isPresent()){
            bearRepository.delete(optionalBear.get());
            return ResponseEntity.ok().body("Bear with ID: " + bearid + " successfully deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bear with ID: " + bearid + " doesn't exist");
    }

    //FORMER SAVE - SAVE METHOD WITH ONLY ONE ENDPOINT - HANDLES ALL POSSIBLE JSON BODIES
    /*if(bear.getZoo() != null){
            if(bear.getZoo().getId() != null){
                Long zooIdRecieved = bear.getZoo().getId();
                Zoo zooToAdd = zooRepository.findById(zooIdRecieved).orElse(null);
                if(zooToAdd == null){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zoo with ID: " + zooIdRecieved + " Doesn't exist");
                }
                bear.setZoo(zooToAdd);
            }
        }*/
}
