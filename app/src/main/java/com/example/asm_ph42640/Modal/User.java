package com.example.asm_ph42640.Modal;

public class User {

    String _id;
    String email;
    String username;
    String password;
    String avatar;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    String fullname;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String _id, String email, String username, String password, String avatar, String fullname) {
        this._id = _id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.fullname = fullname;
    }

    public User(String email, String username, String password, String avatar, String fullname) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.fullname = fullname;
    }

    public User() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
