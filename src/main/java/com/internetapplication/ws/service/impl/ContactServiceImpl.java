package com.internetapplication.ws.service.impl;

import com.internetapplication.ws.Exceptions.BranchNotFoundException;
import com.internetapplication.ws.Exceptions.ContactNotFoundException;
import com.internetapplication.ws.Exceptions.LostUpdatePhenomenonException;
import com.internetapplication.ws.Exceptions.NotUserAbilitiesException;
import com.internetapplication.ws.io.model.ContactEntity;
import com.internetapplication.ws.io.model.UserEntity;
import com.internetapplication.ws.io.repositories.BranchRepository;
import com.internetapplication.ws.io.repositories.ContactRepository;
import com.internetapplication.ws.io.repositories.RoleRepository;
import com.internetapplication.ws.io.repositories.UserRepository;
import com.internetapplication.ws.service.ContactService;
import com.internetapplication.ws.shared.Utils;
import com.internetapplication.ws.shared.dto.ContactDto;
import com.internetapplication.ws.ui.model.request.ContactRequestModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    @Autowired
    private
    UserRepository userRepository;

    @Autowired
    private
    RoleRepository roleRepository;

    @Autowired
    private
    ContactRepository contactRepository;

    @Autowired
    private
    BranchRepository branchRepository;

    @Autowired
    private
    Utils utils;

    @Override
    public ContactDto getContactByContactId(String id) {
        return null;
    }

    @Override
    public List<ContactDto> getContactWithPaginate(int page) {

        UserEntity userEntity = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());

        Page<ContactEntity> contactsPage;

        Pageable contactPageRequest = PageRequest.of(page - 1, 5);

        if (!isAdmin(userEntity)) {
            contactsPage = contactRepository.findAllByBranchId(userEntity.getBranch().getId(), contactPageRequest);
        } else {
            contactsPage = contactRepository.findAll(contactPageRequest);
        }

        List<ContactEntity> contacts = contactsPage.getContent();
        List<ContactDto> returnedValue = new ArrayList<>();


        for (ContactEntity contactEntity : contacts) {
            returnedValue.add(new ModelMapper().map(contactEntity, ContactDto.class));
        }
        return returnedValue;
    }

    @Override
    public ContactDto createContact(ContactDto contactDto, String branchId) {

        ContactEntity contactEntity;
        contactEntity = new ModelMapper().map(contactDto, ContactEntity.class);


        UserEntity userEntity = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());
        if (!isAdmin(userEntity))
            contactEntity.setBranch(userEntity.getBranch());
        else if (branchId == null) throw new RuntimeException("branch Id is Required");
        else contactEntity.setBranch(branchRepository.findByBranchId(branchId));

        contactEntity.setContactId(utils.generateEntityId());
        BeanUtils.copyProperties(contactRepository.save(contactEntity), contactDto);

        return contactDto;
    }

    @Override
    public ContactDto updateContact(String contactId, ContactRequestModel contactRequestModel) {
        ContactEntity contactEntity = contactRepository.findByContactId(contactId);
        if (contactEntity == null) throw new ContactNotFoundException();

        if (!contactEntity.getVersion().equals(contactRequestModel.getVersion()))
            throw new LostUpdatePhenomenonException("contact");

        UserEntity userEntity = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());

        // check if have Authority to modify
        if (!isAdmin(userEntity)) {
            if (contactEntity.getBranch().getId() != (userEntity.getBranch().getId())) {
                throw new NotUserAbilitiesException("you are not able edit this contact");
            }
        } else {
            if (contactRequestModel.getBranchId() != null)
                contactEntity.setBranch(branchRepository.findByBranchId(contactRequestModel.getBranchId()));
        }

        contactEntity.setName(contactRequestModel.getName());
        contactEntity.setPhone(contactRequestModel.getPhone());
        if (isAdmin(userEntity)) {
            contactEntity.setBranch(branchRepository.findByBranchId(contactRequestModel.getBranchId()));
            if (contactEntity.getBranch() == null) throw new BranchNotFoundException();
        }
        contactEntity = contactRepository.save(contactEntity);
        return new ModelMapper().map(contactRepository.findByContactId(contactEntity.getContactId()), ContactDto.class);
    }

    @Override
    public ContactDto mergeContacts(List<ContactDto> contactsDto, ContactRequestModel newContact) {

        UserEntity userEntity = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());
        if (isAdmin(userEntity))
            throw new RuntimeException("this feature is not for Admins");


        checkVersionAndBranch(contactsDto, userEntity.getBranch().getBranchId());
        contactsDto.forEach(this::deleteContact);
        ContactEntity newContactEntity = new ContactEntity();
        newContactEntity.setPhone(newContact.getPhone());
        newContactEntity.setName(newContact.getName());
        newContactEntity.setContactId(utils.generateEntityId());
        newContactEntity.setBranch(userEntity.getBranch());

        newContactEntity = contactRepository.save(newContactEntity);

        return new ModelMapper().map(newContactEntity, ContactDto.class);
    }

    @Override
    public void deleteContact(ContactDto contactDto) {
        contactRepository.delete(contactRepository.findByContactId(contactDto.getContactId()));
    }

    private void checkVersionAndBranch(List<ContactDto> contactsDto, String userBranch) {
        contactsDto.forEach(contact -> {
            ContactEntity contactEntity = contactRepository.findByContactId(contact.getContactId());
            if (!contactEntity.getVersion().equals(contact.getVersion()))
                throw new LostUpdatePhenomenonException("contact " + contact.getContactId());
            if (!contactEntity.getBranch().getBranchId().equals(userBranch)) {
                throw new NotUserAbilitiesException("you are not able to merge outside of your branch");
            }
        });
    }

    private boolean isAdmin(UserEntity userEntity) {
        return userEntity.getRoles().contains(roleRepository.findByName("ROLE_ADMIN"));
    }

    private UserEntity getCurrentUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName());
    }

}
