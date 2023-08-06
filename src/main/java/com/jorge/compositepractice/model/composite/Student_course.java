package com.jorge.compositepractice.model.composite;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@IdClass(StudentCoursePK.class)
public class Student_course {
    // * WE USE "PERSIST" TO CREATE ENTITIES OF THE RELATIONSHIP IF THEY ARE NO PRESENT
    // ? AND "MERGE" TO UPDATE IT WITH THE REST OF ATTRIBUTES GIVEN
    @Id
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "student_id")
    @JsonManagedReference
    private Studentv1 student;
    @Id
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "course_id")
    @JsonManagedReference
    private Coursev1 course;
    LocalDate joinedDate;

    public Student_course() {
    }

    public Student_course(Studentv1 student, Coursev1 course, LocalDate joinedDate) {
        this.student = student;
        this.course = course;
        this.joinedDate = joinedDate;
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
