package com.jorge.compositepractice.controller;

import com.jorge.compositepractice.model.Bear;
import com.jorge.compositepractice.model.DTO.BearDTO;
import com.jorge.compositepractice.repository.BearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bear")
public class BearController {
    @Autowired
    BearRepository bearRepository;

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
            bearDTO.setZooname(b.getZoo().getName());
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

    @GetMapping("/{id}/zoo")
    public ResponseEntity<?> findZooBearIsIn(@PathVariable long id){
        return ResponseEntity.ok(bearRepository.findById(id).get().getZoo());
    }
}
