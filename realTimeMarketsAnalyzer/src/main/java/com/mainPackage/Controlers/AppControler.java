package com.mainPackage.Controlers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppControler {

   @RequestMapping(value = "/home")
    public String home() {
        return "home";
    }
}