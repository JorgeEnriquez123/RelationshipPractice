package com.jorge.compositepractice;

import com.jorge.compositepractice.model.Bear;
import com.jorge.compositepractice.model.Zoo;
import com.jorge.compositepractice.repository.BearRepository;
import com.jorge.compositepractice.repository.ZooRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class CompositepracticeApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(CompositepracticeApplicationTests.class);

    @Autowired
    BearRepository bearRepository;
    @Autowired
    ZooRepository zooRepository;

    @Test
    void prueba() {
        List<Zoo> zooretrieved = zooRepository.findAll();
        for(Zoo z : zooretrieved) {
            System.out.println(z.getName() + ", located at " + z.getAddress());
            //Not getting the List<Bear> that might trigger a loop
        }

        //Getting List<Bear> (Lazy) with findAll turned to EAGER
        List<Zoo> zooWithBears = zooRepository.findAllWithBears();
        for(Zoo z2 : zooWithBears){
            for (Bear b : z2.getBears()){
                System.out.println(b.getName() + " is in " + z2.getName());
                //Not getting the Zoo that might trigger a loop
            }
        }
        // ---------------

        List<Bear> bearretrieved = bearRepository.findAll();
        for(Bear b : bearretrieved) {
            System.out.println(b.getName() + " is a " + b.getSpecies());
        }
        List<Bear> bearWithZoo = bearRepository.findAllWithZoo();
        for(Bear b : bearWithZoo){
            System.out.println(b.getName() + " is in " + b.getZoo().getName());
        }
//        bearList.forEach(bear -> System.out.println(bear.getName() + " " + bear.getSpecies()));
        //System.out.println(bearList);
    }

}
