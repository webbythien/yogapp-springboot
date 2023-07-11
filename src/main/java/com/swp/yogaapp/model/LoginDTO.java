package com.swp.yogaapp.model;

import lombok.Data;

@Data
public class LoginDTO {
    private  String email;
    private  String password;

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


}
