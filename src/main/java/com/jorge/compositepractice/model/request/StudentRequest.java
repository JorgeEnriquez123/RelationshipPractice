package com.jorge.compositepractice.model.request;

import com.jorge.compositepractice.model.composite.Coursev1;

import java.util.ArrayList;
import java.util.List;

public class StudentRequest {
    private String name;
    private List<CourseRequest> courses = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CourseRequest> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseRequest> courses) {
        this.courses = courses;
    }
}
