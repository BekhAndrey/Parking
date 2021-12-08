package com.bekh.parking.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum UserRole implements GrantedAuthority {
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

    private String roleName;

    UserRole(String roleName){
        this.roleName=roleName;
    }
    @Override
    public String getAuthority() {
        return getRoleName();
    }
}
