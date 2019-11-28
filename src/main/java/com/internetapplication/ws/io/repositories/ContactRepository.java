package com.internetapplication.ws.io.repositories;

import com.internetapplication.ws.io.model.ContactEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends PagingAndSortingRepository<ContactEntity, Long> {
}