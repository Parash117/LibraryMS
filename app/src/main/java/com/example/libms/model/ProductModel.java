package com.example.libms.model;

public class ProductModel {

    public String pid, pname, dateofpost, details, categoryId, uid, quantity, author, cost, remaining, primaryimage;

    public ProductModel(String pid, String pname, String dateofpost, String details, String categoryId, String uid, String quantity, String author, String cost, String remaining,String primaryimage) {
        this.pid = pid;
        this.pname = pname;
        this.dateofpost = dateofpost;
        this.details = details;
        this.categoryId = categoryId;
        this.uid = uid;
        this.quantity = quantity;
        this.author = author;
        this.cost = cost;
        this.remaining = remaining;
        this.primaryimage = primaryimage;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPrimaryimage() {
        return primaryimage;
    }

    public void setPrimaryimage(String primaryimage) {
        this.primaryimage = primaryimage;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDateofpost() {
        return dateofpost;
    }

    public void setDateofpost(String dateofpost) {
        this.dateofpost = dateofpost;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }
}
