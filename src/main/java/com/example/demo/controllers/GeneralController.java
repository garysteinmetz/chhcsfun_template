package com.example.demo.controllers;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.example.demo.services.DynamoDBService;
import com.example.demo.services.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


@Controller
public class GeneralController {

    @Autowired
    DynamoDBService dynamoDBService;
    @Autowired
    S3Service s3Service;
    @Value("${tf.var.aws.s3.bucket.name.content}") String contentBucket;

    @GetMapping("/happy.html")
    public ModelAndView happy() throws Exception {
        //
        //System.out.println("ZZZ happy - " + dynamoDBService.getEntryValueAsString());
        //
        return new ModelAndView("happy");
    }
    @ResponseBody
    @RequestMapping(value="*", method=RequestMethod.GET)
    public ResponseEntity<InputStreamResource> general(HttpServletRequest request) throws Exception {
        System.out.println("ZZZ URL Path - " + request.getRequestURI());
        System.out.println("ZZZ URL Context Path - " + request.getContextPath());
        System.out.println("ZZZ URL Path Info - " + request.getPathInfo());
        System.out.println("ZZZ URL Path Translated - " + request.getPathTranslated());
        System.out.println("ZZZ URL Request URL - " + request.getRequestURL());
        System.out.println("ZZZ URL Servlet Path - " + request.getServletPath());
        System.out.println("ZZZ URL Request URI - " + request.getRequestURI());
        System.out.println("ZZZ URL Path Within Handler - " + request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));
        return a(request.getRequestURI());
    }
    @ResponseBody
    @RequestMapping(value="/", method=RequestMethod.GET)
    public ResponseEntity<InputStreamResource> root(HttpServletRequest request) throws Exception {
        System.out.println("ZZZ Root URL Path - " + request.getRequestURI());
        System.out.println("ZZZ Root URL Context Path - " + request.getContextPath());
        System.out.println("ZZZ Root URL Path Info - " + request.getPathInfo());
        System.out.println("ZZZ Root URL Path Translated - " + request.getPathTranslated());
        System.out.println("ZZZ Root URL Request URL - " + request.getRequestURL());
        System.out.println("ZZZ Root URL Servlet Path - " + request.getServletPath());
        System.out.println("ZZZ Root URL Request URI - " + request.getRequestURI());
        System.out.println("ZZZ Root URL Path Within Handler - " + request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));
        return a("/");
    }
    private ResponseEntity<InputStreamResource> a(String requestUri) {
        //
        try {
            final String key = "content" + requestUri;
            System.out.println("ZZZ content - " + key);
            S3Object fileFromBucket = s3Service.getFileFromBucket(contentBucket, key);
            ObjectMetadata objectMetadata = fileFromBucket.getObjectMetadata();
            InputStreamResource inputStreamResource = new InputStreamResource(fileFromBucket.getObjectContent());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentLength(objectMetadata.getContentLength());
            httpHeaders.setContentType(getMediaType(objectMetadata));
            return new ResponseEntity<InputStreamResource>(inputStreamResource, httpHeaders, HttpStatus.OK);
        } catch (AmazonServiceException ase) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    private MediaType getMediaType(ObjectMetadata objectMetadata) {
        MediaType outValue = MediaType.APPLICATION_OCTET_STREAM;
        try {
            //
            outValue = MediaType.parseMediaType(objectMetadata.getContentType());
        } catch (InvalidMediaTypeException imte) {
            //
        }
        return outValue;
    }
}
