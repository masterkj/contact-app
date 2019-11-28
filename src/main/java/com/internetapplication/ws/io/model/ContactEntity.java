package com.internetapplication.ws.io.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name="contacts")
public class ContactEntity implements Serializable {


    private static final long serialVersionUID = -8813117543501074502L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    private String contactId;

    @Column(nullable = false, length = 15)
    private String phone;

    @ManyToOne
    @JoinColumn(name="branch_id")
    private BranchEntity branchEntity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BranchEntity getBranchEntity() {
        return branchEntity;
    }

    public void setBranchEntity(BranchEntity branchEntity) {
        this.branchEntity = branchEntity;
    }
}
