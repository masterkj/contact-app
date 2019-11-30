package com.internetapplication.ws.Exceptions;

public class BranchNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -9018455130517417803L;

    public BranchNotFoundException() {
        super("branch not found");
    }

}
