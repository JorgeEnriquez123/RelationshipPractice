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
public class Coursev1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "course", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JsonBackReference
    private Set<Student_course> studentCourses = new HashSet<>();

    @Override
    public String toString() {
        return "Coursev1{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", studentCourses=" + studentCourses +
                '}';
    }
}
