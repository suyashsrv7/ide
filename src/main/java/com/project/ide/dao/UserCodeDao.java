package com.project.ide.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ide.model.UserCode;

@Repository
public interface UserCodeDao extends JpaRepository<UserCode, String>{

}
