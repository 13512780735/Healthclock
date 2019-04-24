package com.healthclock.healthclock.network.model.user;


import java.io.Serializable;

public class LoginRegisterBean implements Serializable {

    /**
     * id:4
     * token:68773df42e0844b99be377cba925902c
     */
    private int id;
    private String token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}