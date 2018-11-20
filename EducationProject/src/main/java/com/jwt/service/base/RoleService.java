package com.jwt.service.base;

import java.util.List;

import com.jwt.model.RoleModel;

public interface RoleService {
	
	RoleModel addRole(String name);
	
	List<RoleModel> roleList();
	
	RoleModel findByRole(String name);

}
