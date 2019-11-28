package com.internetapplication.ws.shared.dto;

import java.io.Serializable;

public class PrivilegeDto implements Serializable {
    private static final long serialVersionUID = -9046080319789823986L;

    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
