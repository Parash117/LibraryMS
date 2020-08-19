package com.example.libms.model;

public class StudentModel {
    public String sid, name, faculty, phoneno, email, semester, photo;


    public StudentModel(String sid, String name, String faculty, String phoneno, String email, String semester, String photo) {
        this.sid = sid;
        this.name = name;
        this.faculty = faculty;
        this.phoneno = phoneno;
        this.email = email;
        this.semester = semester;
        this.photo = photo;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

