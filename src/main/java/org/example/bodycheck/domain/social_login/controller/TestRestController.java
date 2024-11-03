package org.example.bodycheck.domain.social_login.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class TestRestController {

    @Value("${spring.kakao.client_id}")
    private String client_id_kakao;

    @Value("${spring.kakao.redirect_uri}")
    private String redirect_uri_kakao;

    @Value("${spring.google.client_id}")
    private String client_id_google;

    @Value("${spring.google.redirect_uri}")
    private String redirect_uri_google;

    @GetMapping("/page")
    public String loginPage(Model model) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + client_id_kakao + "&redirect_uri=" + redirect_uri_kakao;
        model.addAttribute("location", location);

        String location2 = "https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=" + client_id_google + "&redirect_uri=" + redirect_uri_google + "&scope=email profile";
        model.addAttribute("location2", location2);

        return "login";
    }
}
