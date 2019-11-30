package com.internetapplication.ws.service;

import com.internetapplication.ws.shared.dto.ContactDto;
import com.internetapplication.ws.ui.model.request.ContactRequestModel;

import java.util.List;

public interface ContactService {
    ContactDto getContactByContactId(String id);
    List<ContactDto> getContactWithPaginate(int page);

    ContactDto createContact(ContactDto contactDto, String branchId);

    ContactDto updateContact(String contactId, ContactRequestModel contactRequestModel);

    ContactDto mergeContacts( List<ContactDto> contactSDto, ContactRequestModel newContact);

    void deleteContact (ContactDto contactDto);
}
