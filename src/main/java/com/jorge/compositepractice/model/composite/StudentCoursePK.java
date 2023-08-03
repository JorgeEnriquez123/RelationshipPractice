package com.jorge.compositepractice.model.composite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class StudentCoursePK {
    @Column(name = "student_id")
    Long studentid;
    @Column(name = "course_id")
    Long courseid;

    public StudentCoursePK() {
    }

    public StudentCoursePK(Long studentid, Long courseid) {
        this.studentid = studentid;
        this.courseid = courseid;
    }

    public Long getStudentid() {
        return studentid;
    }

    public void setStudentid(Long studentid) {
        this.studentid = studentid;
    }

    public Long getCourseid() {
        return courseid;
    }

    public void setCourseid(Long courseid) {
        this.courseid = courseid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentCoursePK that = (StudentCoursePK) o;
        return Objects.equals(studentid, that.studentid) && Objects.equals(courseid, that.courseid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentid, courseid);
    }
}
