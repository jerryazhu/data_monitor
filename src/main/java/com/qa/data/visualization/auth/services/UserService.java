package com.qa.data.visualization.auth.services;

import com.qa.data.visualization.auth.entities.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
