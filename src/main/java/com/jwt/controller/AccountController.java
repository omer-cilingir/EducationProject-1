package com.jwt.controller;

import com.jwt.model.UserModel;
import com.jwt.security.jwt.TokenProvider;
import com.jwt.service.base.UserService;
import java.util.List;
import java.util.Optional;
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

@RestController
@RequestMapping("/api")
//TODO @RequiredArgsConstructor
public class AccountController {

  private final AuthenticationManager authenticationManager;

  private final TokenProvider tokenProvider;

  @Autowired
  UserService userService;

  public AccountController(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
    this.authenticationManager = authenticationManager;
    this.tokenProvider = tokenProvider;
  }

  @PostMapping(path = "/register", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
  public ResponseEntity<?> register(@Valid @RequestBody UserModel user) {

    Optional<UserModel> existModelByUsername = userService.findByUsername(user.getUsername());
    if (existModelByUsername.isPresent()) {
      return new ResponseEntity<>("Username already exist", HttpStatus.BAD_REQUEST);
    } else {
      userService.createUser(user);
      return new ResponseEntity<>(HttpStatus.OK);
    }
  }

  @CrossOrigin
  @PostMapping({"/login"})
  public ResponseEntity<?> authorize(@Valid @RequestBody UserModel loginVM) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        )
    );
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = tokenProvider.generateToken(authentication);
    //TODO login response model ekle token içeren
    return ResponseEntity.ok(jwt);
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
