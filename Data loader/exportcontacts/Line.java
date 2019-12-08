package com.internetapplication.ws.jobs.exportcontacts;

import java.io.Serializable;
import java.time.LocalDate;

public class Line implements Serializable {

    private static final long serialVersionUID = 606753200899251857L;

    private String name;
    private String phone;
    private String branch;

    Line(String name, String phone, String branch) {
        this.name = name;
        this.phone = phone;
        this.branch = branch;
    }

    public Line() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

}