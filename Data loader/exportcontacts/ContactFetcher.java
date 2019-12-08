package com.internetapplication.ws.jobs.exportcontacts;

import com.internetapplication.ws.io.model.ContactEntity;
import com.internetapplication.ws.io.repositories.BranchRepository;
import com.internetapplication.ws.io.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

public class ContactFetcher {
    @Autowired
    ContactRepository contactRepository;

    @Autowired
    BranchRepository branchRepository;

    private static ContactFetcher contactFetcher = null;
    private int currentState;
    private long count;

    public ContactFetcher() {
        this.currentState = 0;
//        this.count = contactRepository.count();
    }

    public Line fetch() {
        if(currentState == count) return null;
        Pageable pageable = PageRequest.of(currentState, 1);
        Page<ContactEntity> contactPage = contactRepository.findAll(pageable);

        List<ContactEntity> contactsListEntity = contactPage.getContent();

        ContactEntity contactEntity = contactsListEntity.get(0);
        String branch = branchRepository.findByBranchId(contactEntity.getBranch().getBranchId()).getName();

        return new Line(contactEntity.getName(), contactEntity.getPhone(),branch);
    }

    public static ContactFetcher getInstance()
    {
        if (contactFetcher == null)
            contactFetcher = new ContactFetcher();

        return contactFetcher;
    }
}
