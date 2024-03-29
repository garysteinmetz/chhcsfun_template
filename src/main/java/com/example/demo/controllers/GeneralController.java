package com.example.demo.controllers;

import com.example.demo.clients.cms.CmsContent;
import com.example.demo.clients.cms.CmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
public class GeneralController {

    @Autowired
    CmsService cmsService;
    @ResponseBody
    @RequestMapping(value="/**", method=RequestMethod.GET)
    public ResponseEntity<InputStreamResource> general(HttpServletRequest request) {
        //System.out.println("ZZZ URL Path - " + request.getRequestURI());
        //System.out.println("ZZZ URL Context Path - " + request.getContextPath());
        //System.out.println("ZZZ URL Path Info - " + request.getPathInfo());
        //System.out.println("ZZZ URL Path Translated - " + request.getPathTranslated());
        //System.out.println("ZZZ URL Request URL - " + request.getRequestURL());
        //System.out.println("ZZZ URL Servlet Path - " + request.getServletPath());
        //System.out.println("ZZZ URL Request URI - " + request.getRequestURI());
        //System.out.println("ZZZ URL Path Within Handler - " + request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));
        return a(request.getRequestURI());
    }
    /*
    For some reason, submitting the root URL path ("/") gets internally translated in Spring Boot
    to "/index", hard code this root URL path to file "/index.html" which is a common default setting
     */
    @ResponseBody
    @RequestMapping(value="/", method=RequestMethod.GET)
    public ResponseEntity<InputStreamResource> root(HttpServletRequest request) {
        //System.out.println("ZZZ Root URL Path - " + request.getRequestURI());
        //System.out.println("ZZZ Root URL Context Path - " + request.getContextPath());
        //System.out.println("ZZZ Root URL Path Info - " + request.getPathInfo());
        //System.out.println("ZZZ Root URL Path Translated - " + request.getPathTranslated());
        //System.out.println("ZZZ Root URL Request URL - " + request.getRequestURL());
        //System.out.println("ZZZ Root URL Servlet Path - " + request.getServletPath());
        //System.out.println("ZZZ Root URL Request URI - " + request.getRequestURI());
        //System.out.println("ZZZ Root URL Path Within Handler - " + request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));
        return a("/index.html");
    }
    private ResponseEntity<InputStreamResource> a(String requestUri) {
        //
        CmsContent content = cmsService.getContent(requestUri);
        if (content != null) {
            InputStreamResource inputStreamResource = new InputStreamResource(content.getContent());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentLength(content.getLength());
            httpHeaders.setContentType(content.getMediaType());
            return new ResponseEntity<>(inputStreamResource, httpHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
