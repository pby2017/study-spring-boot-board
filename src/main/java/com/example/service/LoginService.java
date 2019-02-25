package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Users;
import com.example.repository.UsersRepository;

@Service
public class LoginService {
	
	@Autowired
	private UserPasswordHashClass userPasswordHashClass;
	
	@Autowired
	private UsersRepository usersRepository;
	
	public String login(String userId, String userPw) {
		if(userId.equals("") || userPw.equals("")) {
			return "login";
		}
		
		String hashedPassword = userPasswordHashClass.getSHA256(userPw);
		
		Users users = usersRepository.findByUser_idAndAndUser_pw(userId, hashedPassword);
		if(users == null) {
			return "login";
		}
		
		return "index";
	}
}
