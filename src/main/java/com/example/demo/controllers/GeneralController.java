package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class GeneralController {

    @GetMapping("/happy.html")
    public ModelAndView happy() throws Exception {
        //
        return new ModelAndView("happy");
    }
}
