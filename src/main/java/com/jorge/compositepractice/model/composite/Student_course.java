package com.jorge.compositepractice.model.composite;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Student_course {
    @EmbeddedId
    StudentCoursePK id;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "studentid")
    @MapsId("studentid") //MapsId should have the same name as in StudentCoursePK attributes
    @JsonManagedReference
    private Studentv1 student;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "courseid")
    @MapsId("courseid")
    @JsonManagedReference
    private Coursev1 course;
    LocalDate joinedDate;

    public Student_course() {
    }

    public Student_course(StudentCoursePK id, Studentv1 student, Coursev1 course, LocalDate joinedDate) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.joinedDate = joinedDate;
    }

    public StudentCoursePK getId() {
        return id;
    }

    public void setId(StudentCoursePK id) {
        this.id = id;
    }

    public Studentv1 getStudent() {
        return student;
    }

    public void setStudent(Studentv1 student) {
        this.student = student;
    }

    public Coursev1 getCourse() {
        return course;
    }

    public void setCourse(Coursev1 course) {
        this.course = course;
    }

    public LocalDate getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(LocalDate joinedDate) {
        this.joinedDate = joinedDate;
    }
}
