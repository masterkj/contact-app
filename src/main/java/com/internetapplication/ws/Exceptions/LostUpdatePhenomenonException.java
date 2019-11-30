package com.internetapplication.ws.Exceptions;

public class LostUpdatePhenomenonException extends RuntimeException {
    public LostUpdatePhenomenonException(String entityName) {
        super("sorry, you can't update "+ entityName + " now, pleas reload and try again");
    }
}
