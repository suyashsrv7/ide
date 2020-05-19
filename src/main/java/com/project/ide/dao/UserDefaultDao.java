package com.project.ide.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ide.model.UserDefault;

@Repository
public interface UserDefaultDao extends JpaRepository<UserDefault, Integer>{

}
