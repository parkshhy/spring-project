package org.hdcd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class LoginController {

	//로그인
	@RequestMapping("/login")
	public String loginForm(String error, String logout, Model model) {
		
		//session.setAttribute("CSRF_TOKEN",UUID.randomUUID().toString());

		
		if (error != null) {
			model.addAttribute("error", "로그인 에러");
		}

		if (logout != null) {
			model.addAttribute("logout", "로그아웃 되었습니다.");
		}

		return "auth/loginForm";
	}

	//로그아웃
	@RequestMapping("/logout")
	public String logoutForm() {
		return "auth/logoutForm";
	}

}
