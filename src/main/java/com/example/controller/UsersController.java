package com.example.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.service.JoinService;
import com.example.service.LoginService;

@Controller
public class UsersController {

	@Autowired
	JoinService joinService;

	@Autowired
	LoginService loginService;

	@PostMapping("/joinRequest")
	public String joinRequest(@RequestParam Map<String, String> paramMap) {
		String userId = paramMap.get("user_id");
		String userPw = paramMap.get("user_pw");
		String userName = paramMap.get("user_name");

		String page = joinService.joinUser(userId, userPw, userName);

		return page;
	}

	@PostMapping("/loginRequest")
	public String loginRequest(@RequestParam Map<String, String> paramMap) {
		String userId = paramMap.get("user_id");
		String userPw = paramMap.get("user_pw");

		String page = loginService.login(userId, userPw);

		return page;
	}
}
