package com.internetapplication.ws.Exceptions;

public class ContactNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -8635017101923003014L;

    public ContactNotFoundException() {
        super("contact not found");
    }

}
