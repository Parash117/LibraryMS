package com.example.libms.model;

public class StudentModel {
    public String sid, name, course, phoneno, email, address;

    public StudentModel(String sid, String name, String course, String phoneno, String email, String address) {
        this.sid = sid;
        this.name = name;
        this.course = course;
        this.phoneno = phoneno;
        this.email = email;
        this.address = address;
    }

    public String getSid() {
        return sid;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }
}

