package com.internetapplication.ws.ui.controller;

import com.internetapplication.ws.ErrorMessages;
import com.internetapplication.ws.service.BranchService;
import com.internetapplication.ws.shared.dto.BranchDto;
import com.internetapplication.ws.shared.dto.UserDto;
import com.internetapplication.ws.ui.model.request.BranchRequestModel;
import com.internetapplication.ws.ui.model.response.BranchRest;
import com.internetapplication.ws.ui.model.response.operation.OperationStatusModel;
import com.internetapplication.ws.ui.model.response.operation.RequestOperationName;
import com.internetapplication.ws.ui.model.response.operation.RequestOperationStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("branch")
public class BranchController {

    @Autowired
    BranchService branchService;

    @PostMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public BranchRest createBranch(@RequestBody BranchRequestModel branchRequestModel) throws Exception {
        BranchRest returnedValue = new BranchRest();

        if(branchRequestModel.getName().isEmpty()) throw new Exception(ErrorMessages.MESSING_REQUIRED_FIELD.getErrorMessage());
        BranchDto branchDto = new BranchDto();
        BeanUtils.copyProperties(branchRequestModel, branchDto);
        BeanUtils.copyProperties(branchService.createBranch(branchDto), returnedValue);

        return returnedValue;
    }

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String getBranches(Authentication authentication) throws Exception {
        System.out.println(authentication.getPrincipal());
        return authentication.getName();
    }



    @DeleteMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            path="/{id}")
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setOperationResult(RequestOperationStatus.DELETE.name());

        branchService.deleteBranch(id);

        operationStatusModel.setOperationName(RequestOperationName.SUCCESS.name());

        return operationStatusModel;
    }
}
