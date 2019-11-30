package com.internetapplication.ws.ui.controller;

import com.internetapplication.ws.ErrorMessages;
import com.internetapplication.ws.io.repositories.BranchRepository;
import com.internetapplication.ws.service.ContactService;
import com.internetapplication.ws.shared.dto.BranchDto;
import com.internetapplication.ws.shared.dto.ContactDto;
import com.internetapplication.ws.ui.model.request.ContactRequestModel;
import com.internetapplication.ws.ui.model.response.ContactRest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("contact")
public class ContactController {

    @Autowired
    private
    ContactService contactService;

    @Autowired
    private BranchRepository branchRepository;

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<ContactRest> getContacts(@RequestParam(value = "page", defaultValue = "1") int page) {
        List<ContactDto> contactsDto = contactService.getContactWithPaginate(page);
        List<ContactRest> contactsRest = new ArrayList<ContactRest>();
        for (ContactDto contactDto : contactsDto) {
            ContactRest contactRest = new ContactRest();
            BeanUtils.copyProperties(contactDto, contactRest);
            contactRest.setBranchId(contactDto.getBranch().getBranchId());
            contactsRest.add(contactRest);
        }

        return contactsRest;
    }

    @PostMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ContactRest createContact(@RequestBody ContactRequestModel contactDetails) throws Exception {
        ContactRest returnedValue = new ContactRest();

        if (contactDetails.getName().isEmpty())
            throw new Exception(ErrorMessages.MESSING_REQUIRED_FIELD.getErrorMessage());

        ContactDto contactDto = new ContactDto();
        BeanUtils.copyProperties(contactDetails, contactDto);
        if (contactDetails.getBranchId() != null) {
            BranchDto branchDto = new BranchDto();
            BeanUtils.copyProperties(branchRepository.findByBranchId(contactDetails.getBranchId()), branchDto);
            contactDto.setBranch(branchDto);
        }

        ContactDto createdUser = contactService.createContact(contactDto, contactDetails.getBranchId());
        BeanUtils.copyProperties(createdUser, returnedValue);

        return returnedValue;
    }

    @PutMapping(path = "/{contactId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ContactRest updateContact(@PathVariable String contactId, @RequestBody ContactRequestModel contactRequestModel) {
        ContactDto contactDto = contactService.updateContact(contactId, contactRequestModel);

        ContactRest contactRest = new ContactRest();
        BeanUtils.copyProperties(contactDto, contactRest);

        contactRest.setBranchId(contactDto.getBranch().getBranchId());

        return contactRest;
    }


    @PostMapping(path = "/merge",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ContactRest mergeContacts(@RequestBody List<ContactRequestModel> contacts, @RequestBody ContactRequestModel newContact) {
        // Define the target type
        Type targetListType = new TypeToken<List<ContactDto>>() {
        }.getType();

        ContactDto contactDto = contactService.mergeContacts(new ModelMapper().map(contacts, targetListType), newContact);

        ContactRest returnedValue = new ContactRest();
        BeanUtils.copyProperties(contactDto, returnedValue);

        return returnedValue;

    }

    @DeleteMapping
    public String deleteUser() {
        return "delete user was called";
    }
}
