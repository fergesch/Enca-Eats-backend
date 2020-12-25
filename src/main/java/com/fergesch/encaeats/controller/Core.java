package com.fergesch.encaeats.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class Core {

    @GetMapping("/health")
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public String health() {
      return "I'm alive";
    }
}
