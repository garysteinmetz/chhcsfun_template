package com.example.demo.clients.user;

import com.example.demo.controllers.UserSession;

import java.util.Map;

public interface UserService {
    void saveToUserAppDataTable(String appName, String username, String appData);
    UserData retrieveFromUserAppDataTable(String appName, String username);
}
