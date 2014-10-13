package com.github.yasbc.controllers;

import com.github.yasbc.utils.Auditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class EntranceController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        Auditor.entry("EntranceController.index()");

        return "index";
    }
}
