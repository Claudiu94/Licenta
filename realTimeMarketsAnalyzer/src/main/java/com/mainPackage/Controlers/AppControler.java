package com.mainPackage.Controlers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by claudiu on 02.04.2017.
 */
@Controller
public class AppControler {

    @RequestMapping(value = "/home")
    public String home() {
        return "home";
    }
}
