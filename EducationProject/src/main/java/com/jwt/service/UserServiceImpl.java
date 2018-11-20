package com.jwt.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.enums.SystemConstants;
import com.jwt.mail.NotificationService;
import com.jwt.model.RoleModel;
import com.jwt.model.UserModel;
import com.jwt.repository.RoleRepository;
import com.jwt.repository.UserRepository;
import com.jwt.service.base.UserService;
import com.jwt.util.DateUtil;
import com.jwt.util.RandomUtil;

@Service
public class UserServiceImpl implements UserService {

	public static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	NotificationService notificationService;

	@Override
	public UserModel createUser(UserModel model) {
		Set<RoleModel> roles = new HashSet<>();
		List<RoleModel> allRoles = roleRepository.findAll();
		for (RoleModel roleModel : allRoles) {
			if (roleModel.getName().equalsIgnoreCase(model.getRoles().iterator().next().getName())) {
				roles.add(roleModel);
			}
		}
		UserModel user = new UserModel();
		user.setUsername(model.getUsername());
		user.setEmail(model.getEmail());
		user.setPassword(passwordEncoder.encode(model.getPassword()));
		user.setActive(false);
		user.setActivationKey(RandomUtil.generateActivationKey());
		user.setCreatedDate(DateUtil.now());
		user.setModifiedDate(DateUtil.now());
		user.setCreatedUser(SystemConstants.SYSTEM.toString());
		user.setModifiedUser(SystemConstants.SYSTEM.toString());
		user.setRoles(roles);
		userRepository.save(user);

		try {
			notificationService.sendEmail(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return user;
	}

	@Override
	public List<UserModel> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public Optional<UserModel> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public UserModel activateRegistration(String key) {
		UserModel user = userRepository.findByActivationKey(key);
		user.setActivationKey(null);
		user.setActive(true);
		userRepository.save(user);
		return user;
	}

	@Override
	public Optional<UserModel> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

}
