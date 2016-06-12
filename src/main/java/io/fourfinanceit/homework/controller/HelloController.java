package io.fourfinanceit.homework.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@RequestMapping(value = "/hello", method = GET)
    public String index() {
        return "Welcome to 4financeit! :) ";
    }
}
