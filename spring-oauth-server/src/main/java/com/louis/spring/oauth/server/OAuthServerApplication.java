package com.louis.spring.oauth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@SpringBootApplication
public class OAuthServerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(OAuthServerApplication.class, args);
    }


    @RequestMapping("/user2")
    public Principal user(Principal principal) {
        System.out.println(principal);
        return principal;
    }
}
