package com.internetapplication.ws.io.repositories;

import com.internetapplication.ws.io.model.ContactEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends PagingAndSortingRepository<ContactEntity, Long> {
    Page<ContactEntity> findAllByBranchId(Long branchId, Pageable pageable);

    ContactEntity findByContactId(String contactId);
}