package com.example.demo.controllers;

import com.amazonaws.services.s3.model.S3Object;
import com.example.demo.services.CognitoService;
import com.example.demo.services.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AuthorController {
    @Value("${aws.cognito.group.developers}") String developersGroup;
    @Autowired
    CognitoService cognitoService;
    @Autowired
    S3Service s3Service;
    @Value("${aws.s3.bucket.name.content}") String contentBucket;
    @GetMapping("/")
    public ModelAndView index() throws Exception {
        //
        return new ModelAndView("index");
    }
    @ResponseBody
    @GetMapping("/developersList")
    public ResponseEntity developerList() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> cognitoUsersInGroup = cognitoService.getCognitoUsersInGroup(developersGroup);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(objectMapper.writeValueAsString(cognitoUsersInGroup), headers, HttpStatus.OK);
    }
    @GetMapping("/content/{author}")
    public ModelAndView saveAppState(@PathVariable String author) throws Exception {
        return new ModelAndView("content");
    }
    @ResponseBody
    @GetMapping("/content/{author}/{content}")
    public ResponseEntity getContent(@PathVariable String author, @PathVariable String content) throws Exception {
        S3Object s3Object = s3Service.getFileFromBucket(contentBucket, author + "/" + content);
        byte[] contentBytes = s3Service.readContent(s3Object);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(contentBytes, headers, HttpStatus.OK);
    }
    @ResponseBody
    @GetMapping("/contentList")
    public ResponseEntity developerList(@RequestParam("developer") String developer) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        //List<String> cognitoUsersInGroup = cognitoService.getCognitoUsersInGroup(developersGroup);
        List<String> contentList = s3Service.listFilesInFolder(contentBucket, developer + "/");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(objectMapper.writeValueAsString(contentList), headers, HttpStatus.OK);
    }
}
