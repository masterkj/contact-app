package com.internetapplication.ws.service;

import com.internetapplication.ws.shared.dto.BranchDto;

public interface BranchService {
    BranchDto createBranch (BranchDto branchDto);
    void deleteBranch(String branchId);
}
