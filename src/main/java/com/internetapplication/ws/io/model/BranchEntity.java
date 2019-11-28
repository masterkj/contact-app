package com.internetapplication.ws.io.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "branches")
public class BranchEntity implements Serializable {


    private static final long serialVersionUID = -6926090091387201208L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;


    @Column(nullable = false, unique = true)
    private String branchId;

    @Column(nullable = false, length = 40)
    private String location;

    @OneToMany(mappedBy = "branchEntity", cascade = CascadeType.ALL)
    private List<ContactEntity> contacts;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<ContactEntity> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactEntity> contacts) {
        this.contacts = contacts;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
}
