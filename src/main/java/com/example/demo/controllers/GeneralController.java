package com.example.demo.controllers;

import com.example.demo.services.DynamoDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class GeneralController {

    @Autowired
    DynamoDBService dynamoDBService;

    @GetMapping("/happy.html")
    public ModelAndView happy() throws Exception {
        //
        //System.out.println("ZZZ happy - " + dynamoDBService.getEntryValueAsString());
        //
        return new ModelAndView("happy");
    }
}
