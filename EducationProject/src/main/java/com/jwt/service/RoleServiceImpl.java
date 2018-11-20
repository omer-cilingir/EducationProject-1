package com.jwt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwt.enums.SystemConstants;
import com.jwt.model.RoleModel;
import com.jwt.repository.RoleRepository;
import com.jwt.security.SecurityUtils;
import com.jwt.service.base.RoleService;
import com.jwt.util.DateUtil;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleRepository roleRepository;

	@Override
	public RoleModel addRole(String name) {
		String username = SecurityUtils.getCurrentUsername();
		RoleModel role = new RoleModel();
		role.setActive(true);
		role.setCreatedDate(DateUtil.now());
		role.setCreatedUser(username != null ? username : SystemConstants.SYSTEM.toString());
		role.setModifiedDate(DateUtil.now());
		role.setModifiedUser(username != null ? username : SystemConstants.SYSTEM.toString());
		role.setName(name);
		roleRepository.save(role);
		return role;
	}

	@Override
	public List<RoleModel> roleList() {
		return roleRepository.findAll();
	}

	@Override
	public RoleModel findByRole(String name) {
		return roleRepository.findByName(name);
	}
}
