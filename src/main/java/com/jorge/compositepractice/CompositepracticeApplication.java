package com.jorge.compositepractice;

import com.jorge.compositepractice.model.Bear;
import com.jorge.compositepractice.model.Zoo;
import com.jorge.compositepractice.model.composite.Coursev1;
import com.jorge.compositepractice.model.composite.Studentv1;
import com.jorge.compositepractice.repository.BearRepository;
import com.jorge.compositepractice.repository.Coursev1Repository;
import com.jorge.compositepractice.repository.Studentv1Repository;
import com.jorge.compositepractice.repository.ZooRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class CompositepracticeApplication implements CommandLineRunner {
    private static Logger LOG = LoggerFactory.getLogger(CompositepracticeApplication.class);
    @Autowired
    BearRepository bearRepository;
    @Autowired
    ZooRepository zooRepository;
    @Autowired
    Studentv1Repository studentv1Repository;
    @Autowired
    Coursev1Repository coursev1Repository;

    public static void main(String[] args) {
        SpringApplication.run(CompositepracticeApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        LOG.info("\n CREATING ZOO");
        Zoo zoo = new Zoo();
        zoo.setName("WildScape Zoo");
        zoo.setAddress("123 Main Street");

        LOG.info("\n CREATING BEAR1");
        Bear bear1 = new Bear();
        bear1.setName("Grizz");
        bear1.setSpecies("Grizzly Bear");
        bear1.setZoo(zoo); //ESTABLISHING THE ASSOCIATION - IMPORTANT !!!

        LOG.info("\n CREATING BEAR2");
        Bear bear2 = new Bear();
        bear2.setName("Polar");
        bear2.setSpecies("Polar Bear");
        bear2.setZoo(zoo); //ESTABLISHING THE ASSOCIATION - IMPORTANT !!!

        LOG.info("\n ADDING BEARS TO THE ZOO");
        List<Bear> bears = Arrays.asList(bear1, bear2);
        zoo.setBears(bears);

        LOG.info("\n SAVING ZOO TO DB");
        zooRepository.save(zoo);

        /*LOG.info("\n TRYING TO SAVE FROM BEAR CREATION");
        Bear bear3 = new Bear();
        bear3.setName("Guri");
        bear3.setSpecies("Grizzly Bear");
        LOG.info("\n BEAR CREATED");

        LOG.info("\n CREATING ZOO");
        Zoo zoo2 = new Zoo();
        zoo2.setName("High Zoo");
        zoo2.setAddress("Buenvista");
        LOG.info("\n ZOO CREATED");
        LOG.info("\n SETTING ZOO");
        bear3.setZoo(zoo2);
        LOG.info("\n SAVING BEAR WITH ZOO");
        bearRepository.save(bear3);*/


        LOG.info("\n -- COMPOSITE PRACTICE --");

        LOG.info("\n CREATING STUDENT");
        Studentv1 std1 = new Studentv1();
        std1.setName("Jorge");

        Studentv1 std2 = new Studentv1();
        std2.setName("Erick");
        studentv1Repository.save(std2);

        LOG.info("\n CREATING COURSE 1");
        Coursev1 crs1 = new Coursev1();
        crs1.setName("Math");


        LOG.info("\n CREATING COURSE 2");
        Coursev1 crs2 = new Coursev1();
        crs2.setName("English");


        LOG.info("\n SETTING COURSES TO THE STUDENT");
        std1.getCourses().add(crs1);
        std1.getCourses().add(crs2);

        //LOG.info("\n SETTING THE STUDENT IN THE COURSES");
        //It is not necessary to set it from the child side

        /*crs1.getStudents().add(std1);
        crs2.getStudents().add(std1);*/

        LOG.info("\n SAVING THE STUDENT (TAKING ADVANTAGE OF CASCADE)");
        studentv1Repository.save(std1);
        LOG.info("\n STUDENT SAVED");
    }
}
