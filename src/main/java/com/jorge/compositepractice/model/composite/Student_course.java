package com.jorge.compositepractice.model.composite;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class Student_course {
    @EmbeddedId
    StudentCoursePK studentCoursePK;
    LocalDate joinedDate;

}
