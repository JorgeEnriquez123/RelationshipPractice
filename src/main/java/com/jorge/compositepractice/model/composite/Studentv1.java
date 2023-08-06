package com.jorge.compositepractice.model.composite;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Studentv1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "student", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JsonBackReference
    private Set<Student_course> studentCourses = new HashSet<>();
    // * THIS MAKE THE RELATION PERSISTING EASIER
    // ? INSTEAD OF PERSISTING THE STUDDENT_COURSE INDIVUDALLY...
    // * WE SET A LIST OF: -STUDENT- + -COURSE ASSIGNED-
    // ! EXAMPLE:
    // Student_Course studentCourse1 = new StudentCourse(student1, course1, localdate);
    // Student_Course studentCourse2 = new StudentCourse(student1, course2, localdate);
    // student.getStudentCourses.add(studentCourse1)
    // student.getStudentCourses.add(studentCourse2)


    @Override
    public String toString() {
        return "Studentv1{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", studentCourses=" + studentCourses +
                '}';
    }
}
