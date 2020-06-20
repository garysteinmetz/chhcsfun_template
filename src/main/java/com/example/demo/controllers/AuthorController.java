package com.example.demo.controllers;

import com.amazonaws.services.s3.model.S3Object;
import com.example.demo.services.CognitoService;
import com.example.demo.services.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class AuthorController {
    @Value("${aws.cognito.group.developers}") String developersGroup;
    @Value("${aws.s3.bucket.perUserLimit}") int perUserLimit;
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
    @PostMapping("/content")
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile file, HttpSession httpSession
        ) throws Exception {
        Optional<UserSession> userSession = UserSession.getSession(httpSession);
        if (userSession.isPresent() && cognitoService.isUserInGroup(userSession.get(), developersGroup)) {
            String contentPrefix = userSession.get().getUsername() + "/";
            List<String> fileNames = s3Service.listFilesInFolder(contentBucket, contentPrefix);
            String key = contentPrefix + file.getOriginalFilename();
            if (fileNames.size() < perUserLimit) {
                //
                s3Service.uploadFileIntoBucket(
                        contentBucket, key, file.getInputStream(), file.getContentType());
            } else if (fileNames.size() == perUserLimit && fileNames.contains(file.getOriginalFilename())) {
                //
                s3Service.uploadFileIntoBucket(
                        contentBucket, key, file.getInputStream(), file.getContentType());
            } else {
                //
            }
        }
        return new ModelAndView("redirect:/");
    }
    @ResponseBody
    @GetMapping("/content/{author}/{content}")
    public ResponseEntity getContent(@PathVariable String author, @PathVariable String content) throws Exception {
        S3Object s3Object = s3Service.getFileFromBucket(contentBucket, author + "/" + content);
        byte[] contentBytes = s3Service.readContent(s3Object);
        HttpHeaders headers = new HttpHeaders();
        if (s3Object.getObjectMetadata() != null && s3Object.getObjectMetadata().getContentType() != null) {
            headers.add(HttpHeaders.CONTENT_TYPE, s3Object.getObjectMetadata().getContentType());
        }
        return new ResponseEntity<>(contentBytes, headers, HttpStatus.OK);
    }
    @ResponseBody
    @GetMapping("/contentList")
    public ResponseEntity developerList(
            @RequestParam(name="developer", required=false) String developer,
            HttpSession httpSession) throws Exception {
        ResponseEntity outValue;
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<UserSession> userSession = UserSession.getSession(httpSession);
        String pathPrefix = null;
        if (developer != null) {
            pathPrefix = developer;
        } else if (userSession.isPresent() && cognitoService.isUserInGroup(userSession.get(), developersGroup)) {
            pathPrefix = userSession.get().getUsername();
        }
        //
        if (pathPrefix != null) {
            //
            List<String> contentList = s3Service.listFilesInFolder(contentBucket, pathPrefix + "/");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            outValue = new ResponseEntity<>(objectMapper.writeValueAsString(contentList), headers, HttpStatus.OK);
        } else {
            outValue = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return outValue;
    }
}
