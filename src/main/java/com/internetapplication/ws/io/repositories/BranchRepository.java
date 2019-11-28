package com.internetapplication.ws.io.repositories;

import com.internetapplication.ws.io.model.BranchEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends PagingAndSortingRepository<BranchEntity, Long> {
    BranchEntity findByName(String branchName);

    BranchEntity findByBranchId(String branchId);
}