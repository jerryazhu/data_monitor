package com.qa.data.visualization.auth.services;

public interface SecurityService {
    String findLoggedInUsername();

    void autologin(String username, String password);
}
