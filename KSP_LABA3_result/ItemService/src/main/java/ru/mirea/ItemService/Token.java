package ru.mirea.ItemService;

public class Token {
    private  int id;
    private String login;
    private String role;
    private String signature;

    public Token(){}

    public Token(int id, String login, String role, String signature) {
        this.id = id;
        this.login = login;
        this.role = role;
        this.signature = signature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}