package ru.mirea.Identity;

import ru.mirea.Tokens.Role;

public class User {
    private int id;
    private String login;
    private String password;
    private Role role;

    public User() {}

    public User(int id, String login, String password, String role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = Role.valueOf(role);
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public Role getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

}
