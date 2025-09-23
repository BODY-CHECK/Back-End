package org.example.bodycheck.external.oauth2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Deprecated
@Controller
@RequestMapping("/login")
public class Oauth2TestController { // 테스트 용

	@Value("${spring.kakao.client_id}")
	private String clientIdKakao;

	@Value("${spring.kakao.redirect_uri}")
	private String redirectUriKakao;

	@Value("${spring.google.client_id}")
	private String clientIdGoogle;

	@Value("${spring.google.redirect_uri}")
	private String redirectUriGoogle;

	@GetMapping("/page")
	public String loginPage(Model model) {
		String location =
			"https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + clientIdKakao
				+ "&redirect_uri=" + redirectUriKakao;
		model.addAttribute("location", location);

		String location2 =
			"https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=" + clientIdGoogle
				+ "&redirect_uri=" + redirectUriGoogle + "&scope=email profile";
		model.addAttribute("location2", location2);

		return "login";
	}
}
