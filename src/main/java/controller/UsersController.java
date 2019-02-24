package controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;

import repository.UsersRepository;
import service.JoinService;

public class UsersController {

	private UsersRepository usersRepository;
	
	@PostMapping(value="/joinRequest")
	public String joinRequest(HttpServletRequest request) {
		JoinService joinService = new JoinService();
		joinService.joinUser(request, usersRepository);
		
		return "index";
	}
	
	@PostMapping(value="/loginRequest")
	public String loginRequest() {
		return "index";
	}
}
