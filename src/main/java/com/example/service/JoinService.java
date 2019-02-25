package com.example.service;

import javax.servlet.http.HttpServletRequest;

import com.example.model.Users;
import com.example.repository.UsersRepository;

public class JoinService {
	public void joinUser(HttpServletRequest request, UsersRepository usersRepository) {
		String userId = request.getParameter("user_id");
		String userPw = request.getParameter("user_password");
		String userName = request.getParameter("user_name");
		
		Users users = new Users();
		users.setUser_id(userId);
		users.setUser_pw(userPw);
		users.setUser_name(userName);
		
		usersRepository.save(users);
	}
}
