package com.internetapplication.ws.shared.dto;

import java.io.Serializable;
import java.util.Collection;

public class RoleDto implements Serializable {

    private static final long serialVersionUID = 6143701274305598992L;

    private long id;
    private String name;
    private Collection<PrivilegeDto> privileges;

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

    public Collection<PrivilegeDto> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Collection<PrivilegeDto> privileges) {
        this.privileges = privileges;
    }
}
