package org.example;

public class Token {
    private String token;
    private String address;

    public Token(String token, String address) {
        this.token = token;
        this.address = address;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
