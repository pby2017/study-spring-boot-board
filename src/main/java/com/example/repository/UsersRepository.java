package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Users;

public interface UsersRepository extends JpaRepository<Users, Long>{
	public Users findByUser_idAndAndUser_pw(String userId, String userPw);
}
