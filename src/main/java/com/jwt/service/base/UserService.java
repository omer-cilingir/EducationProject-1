package com.jwt.service.base;

import java.util.List;
import java.util.Optional;

import com.jwt.model.UserModel;

public interface UserService {
	
	UserModel createUser(UserModel model);
	
	List<UserModel> getAllUsers();
	
	Optional<UserModel> findByUsername(String username);
	
	UserModel activateRegistration(String key);
	
	Optional<UserModel> findByEmail(String email);

}
