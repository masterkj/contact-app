package com.internetapplication.ws.service.impl;

import com.internetapplication.ws.io.model.BranchEntity;
import com.internetapplication.ws.io.repositories.BranchRepository;
import com.internetapplication.ws.service.BranchService;
import com.internetapplication.ws.shared.utils.Utils;
import com.internetapplication.ws.shared.dto.BranchDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BranchServiceImpl implements BranchService {

    @Autowired
    private
    BranchRepository branchRepository;

    @Autowired
    private
    Utils utils;

    @Override
    public BranchDto createBranch(BranchDto branch) {
        BranchEntity branchEntity = new BranchEntity();
        if(existedBranch(branch))  throw new RuntimeException("the branch is already existed");

        BeanUtils.copyProperties(branch, branchEntity);

        branchEntity.setBranchId(utils.generateEntityId());

        BranchDto returnedValue = new BranchDto();

        BeanUtils.copyProperties(branchRepository.save(branchEntity), returnedValue);


        return returnedValue;
    }

    @Override
    public void deleteBranch(String branchId) {
        BranchEntity branchEntity = branchRepository.findByBranchId(branchId);

        if(branchEntity == null)  throw new RuntimeException("the branch is not existed");

        branchRepository.delete(branchEntity);

    }




    /**
     * check if Branch name existed
     */
    private boolean existedBranch(BranchDto branch) {
        return (branchRepository.findByName(branch.getName()) != null);

    }

}
