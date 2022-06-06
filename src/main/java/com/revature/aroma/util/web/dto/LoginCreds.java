package com.revature.aroma.util.web.dto;

public class LoginCreds {

    private String username;
    private String password;
    private String isAdmin;

    // JACKSON REQUIRES A NO ARG CONSTRUCTOR

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }
}
