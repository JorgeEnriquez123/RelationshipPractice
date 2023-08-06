package com.jorge.compositepractice.model.request;

import com.jorge.compositepractice.model.composite.Coursev1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CourseRequest {
    private String name;
    private LocalDate localDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
