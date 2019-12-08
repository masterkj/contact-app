package com.internetapplication.ws.ui.model.request;

import java.util.Collection;

public class MergeRequestModel {
    private Collection<ContactRequestModel> contacts;
    private ContactRequestModel newContact;

    public Collection<ContactRequestModel> getContacts() {
        return contacts;
    }

    public void setContacts(Collection<ContactRequestModel> contacts) {
        this.contacts = contacts;
    }

    public ContactRequestModel getNewContact() {
        return newContact;
    }

    public void setNewContact(ContactRequestModel newContact) {
        this.newContact = newContact;
    }
}
