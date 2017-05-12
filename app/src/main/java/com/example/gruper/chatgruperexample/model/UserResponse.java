package com.example.gruper.chatgruperexample.model;

import java.util.List;

/**
 * Created by gruper on 09/05/17.
 */

public class UserResponse {
    private List<User> user;

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "user=" + user +
                '}';
    }
}
