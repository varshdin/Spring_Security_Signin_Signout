package com.smart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.main.User;

public interface UserRepository extends JpaRepository<User, Integer> {
User findByUsernameOrEmail(String username, String email);
}