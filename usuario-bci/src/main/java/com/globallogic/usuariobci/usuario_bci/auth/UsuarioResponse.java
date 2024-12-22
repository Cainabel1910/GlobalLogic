package com.globallogic.usuariobci.usuario_bci.auth;

import com.globallogic.usuariobci.usuario_bci.model.Telefono;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UsuarioResponse {
    private UUID id;
    private Date created;
    private Date lastLogin;

    private String token;
    private boolean isActive;

    private String name;

    private String email;
    private String password;
    private List<Telefono> phones;

    public UsuarioResponse() {
    }

    public UsuarioResponse(UUID id, Date created, Date lastLogin, String token, boolean isActive, String name, String email, String password, List<Telefono> phones) {
        this.id = id;
        this.created = created;
        this.lastLogin = lastLogin;
        this.token = token;
        this.isActive = isActive;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phones = phones;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
