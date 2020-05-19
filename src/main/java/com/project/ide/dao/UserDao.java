package com.project.ide.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ide.model.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
	User findByUsername(String username);
	User findByEmail(String email);

}
