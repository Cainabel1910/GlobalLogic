package com.globallogic.usuariobci.usuario_bci.auth;

import com.globallogic.usuariobci.usuario_bci.model.Telefono;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class RegisterRequest {
    private String name;
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private List<Telefono> phones;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Telefono> getPhones() {
        return phones;
    }

    public void setPhones(List<Telefono> phones) {
        this.phones = phones;
    }
}
