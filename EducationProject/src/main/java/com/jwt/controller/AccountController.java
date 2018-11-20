package com.jwt.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.model.UserModel;
import com.jwt.security.jwt.JWTConfigurer;
import com.jwt.security.jwt.TokenProvider;
import com.jwt.service.base.UserService;
import com.jwt.web.vm.JWTToken;

@RestController
@RequestMapping("/api")
public class AccountController {

	private final AuthenticationManager authenticationManager;

	private final TokenProvider tokenProvider;

	@Autowired
	UserService userService;

	public AccountController(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
		this.authenticationManager = authenticationManager;
		this.tokenProvider = tokenProvider;
	}

	@PostMapping(path = "/register", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<?> register(@Valid @RequestBody UserModel user) {

		Optional<UserModel> existModelByUsername = userService.findByUsername(user.getUsername());
		if (existModelByUsername.isPresent())
			return new ResponseEntity<>("Username already exist", HttpStatus.BAD_REQUEST);
		else {
			userService.createUser(user);
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	@CrossOrigin
	@PostMapping({ "/login" })
	public ResponseEntity<?> authorize(@Valid @RequestBody UserModel loginVM, HttpServletResponse response) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginVM.getUsername(), loginVM.getPassword());
		Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.createToken(authentication);
		response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, jwt);
		return ResponseEntity.ok(new JWTToken(jwt));
	}

	@GetMapping(path = "/allUsers")
	public List<UserModel> getAllUserList() {
		return userService.getAllUsers();
	}

	@GetMapping(path = "/user/{username}")
	public Optional<UserModel> getByUserName(@PathVariable String username) {
		return userService.findByUsername(username);
	}

	@GetMapping(path = "/activate")
	public UserModel activateUser(@RequestParam(value = "key") String key) {
		UserModel userModel = userService.activateRegistration(key);
		return userModel;
	}
}
