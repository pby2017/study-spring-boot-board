package com.example.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {

	@Autowired
	HttpSession session;

	public String logout() {
		
		session.invalidate();

		return "index";
	}
}
