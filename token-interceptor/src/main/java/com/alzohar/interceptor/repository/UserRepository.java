package com.alzohar.interceptor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alzohar.interceptor.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByUsername(String username);

	@Query("Select u from User u WHERE u.username=:username")
	public User getUserByUsername(@Param("username") String username);
}
