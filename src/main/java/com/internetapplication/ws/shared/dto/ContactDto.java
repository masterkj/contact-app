package com.internetapplication.ws.shared.dto;

public class ContactDto {
    private long id;
    private String contactId;
    private String phone;
    private BranchDto branchDto;

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

    public BranchDto getBranchDto() {
        return branchDto;
    }

    public void setBranchDto(BranchDto branchDto) {
        this.branchDto = branchDto;
    }
}
