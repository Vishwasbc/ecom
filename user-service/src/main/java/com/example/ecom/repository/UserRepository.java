package com.example.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecom.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
