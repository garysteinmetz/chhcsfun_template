package com.example.demo.clients.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

public class LocalUserService implements UserService {

    @Value("${local.user.data}")
    String localUserData;
    UserData userData;
    @PostConstruct
    public void init() throws JsonProcessingException {
        System.out.println("ZZZ LocalUserData - " + localUserData);
        ObjectMapper objectMapper = new ObjectMapper();
        userData = objectMapper.readValue(localUserData, UserData.class);
    }
    public void saveToUserAppDataTable(String appName, String username, String appData) {
        userData.isPresent = true;
        userData.appData = appData;
        userData.lastModified = System.currentTimeMillis();
    }
    public UserData retrieveFromUserAppDataTable(String appName, String username) {
        return userData;
    }
}
