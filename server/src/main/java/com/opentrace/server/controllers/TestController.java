package com.opentrace.server.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping
public class TestController {

    @GetMapping("/test")
    @ResponseBody
    public String Cheack() {
        System.out.println("accept req on rout /");

        return "ok";
    }
}