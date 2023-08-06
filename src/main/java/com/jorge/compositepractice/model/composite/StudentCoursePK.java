package com.jorge.compositepractice.model.composite;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

public class StudentCoursePK implements Serializable {
    Long student;
    Long course;

    public StudentCoursePK() {
    }

    public StudentCoursePK(Long student, Long course) {
        this.student = student;
        this.course = course;
    }

    public Long getStudent() {
        return student;
    }

    public void setStudent(Long student) {
        this.student = student;
    }

    public Long getCourse() {
        return course;
    }

    public void setCourse(Long course) {
        this.course = course;
    }
}
