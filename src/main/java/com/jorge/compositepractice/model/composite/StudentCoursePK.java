package com.jorge.compositepractice.model.composite;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StudentCoursePK implements Serializable {
    long studentid;
    long courseid;

    public StudentCoursePK() {
    }

    public StudentCoursePK(long studentid, long courseid) {
        this.studentid = studentid;
        this.courseid = courseid;
    }

    public long getStudentid() {
        return studentid;
    }

    public void setStudentid(long studentid) {
        this.studentid = studentid;
    }

    public long getCourseid() {
        return courseid;
    }

    public void setCourseid(long courseid) {
        this.courseid = courseid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentCoursePK that = (StudentCoursePK) o;
        return studentid == that.studentid && courseid == that.courseid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentid, courseid);
    }
}
