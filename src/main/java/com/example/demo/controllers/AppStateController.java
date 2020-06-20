package com.example.demo.controllers;

import com.example.demo.services.DynamoDBService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

@Controller
public class AppStateController {
    @Autowired
    DynamoDBService dynamoDBService;
    @ResponseBody
    @PostMapping("/appState/{author}/{appName}")
    public ResponseEntity saveAppState(@PathVariable String author, @PathVariable String appName,
                                       @RequestParam("appData") String data, HttpSession httpSession) throws Exception {
        //
        System.out.println("ZZZ Received - " + author);
        System.out.println("ZZZ Received - " + appName);
        System.out.println("ZZZ Received - " + data);
        Optional<UserSession> userSession = UserSession.getSession(httpSession);
        if (userSession.isPresent()) {
            dynamoDBService.saveToUserAppDataTable(author, appName, userSession.get().getUsername(), data);
        } else {
            throw new IllegalStateException("User isn't logged in");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.add("Content-Type", "application/json");
        return new ResponseEntity<>("{}", headers, HttpStatus.OK);
        //return "{}";
    }
    @ResponseBody
    @GetMapping("/appState/{author}/{appName}")
    public ResponseEntity getAppStateForUser(@PathVariable String author, @PathVariable String appName,
        HttpSession httpSession) throws Exception {
        //
        String outValue;
        System.out.println("ZZZ Received - " + author);
        System.out.println("ZZZ Received - " + appName);
        //System.out.println("ZZZ Received - " + data);
        Optional<UserSession> userSession = UserSession.getSession(httpSession);
        if (userSession.isPresent()) {
            Map<String, Object> stringObjectMap = dynamoDBService.retrieveFromUserAppDataTable(
                    author, appName, userSession.get().getUsername());
            ObjectMapper objectMapper = new ObjectMapper();
            outValue = objectMapper.writeValueAsString(stringObjectMap);
        } else {
            throw new IllegalStateException("User isn't logged in");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(outValue, headers, HttpStatus.OK);
        //return "{}";
    }
    @ResponseBody
    @GetMapping("/appState/{author}/{appName}/allUsers")
    public ResponseEntity getAppStateForAllUsers(@PathVariable String author, @PathVariable String appName
        ) throws Exception {
        //
        String outValue;
        ObjectNode o = dynamoDBService.retrieveAppDataForAllUsersAsJsonObject(author, appName);
        ObjectMapper objectMapper = new ObjectMapper();
        outValue = objectMapper.writeValueAsString(o);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(outValue, headers, HttpStatus.OK);
    }
}
