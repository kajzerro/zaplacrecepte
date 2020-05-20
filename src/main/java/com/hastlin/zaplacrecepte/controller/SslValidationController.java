package com.hastlin.zaplacrecepte.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SslValidationController {

    @GetMapping
    @RequestMapping(value = "/.well-known/pki-validation/4FE5F91B6C76A35DAFE583C490C23A5D.txt")
    String login() {
        return "80554E6690933626FE84B45963EF0D51C4E0CEA202C6651407A869E5B516D6C0\n" +
                "comodoca.com\n" +
                "adc7ce308dad549";
    }

}

