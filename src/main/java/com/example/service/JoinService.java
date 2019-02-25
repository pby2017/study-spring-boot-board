package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Users;
import com.example.repository.UsersRepository;

@Service
public class JoinService {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private UserPasswordHashClass userPasswordHashClass;

	public String joinUser(String userId, String userPw, String userName) {

		if (userId.equals("") || userPw.equals("") || userName.equals("")) {
			return "join";
		}

		Users users = new Users();
		users.setUser_id(userId);
		String hashedPassword = userPasswordHashClass.getSHA256(userPw);
		users.setUser_pw(hashedPassword);
		users.setUser_name(userName);

		usersRepository.save(users);
		return "index";
	}
}
