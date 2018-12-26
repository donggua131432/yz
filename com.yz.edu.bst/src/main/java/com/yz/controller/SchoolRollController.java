package com.yz.controller;


import com.yz.core.annotation.Rule;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/schoolRoll")
public class SchoolRollController {

    @RequestMapping("/index")
    @Rule
    public String schoolRoll() {
        return "schoolRoll";
    }

}
