package com.jwt.security;

import com.jwt.model.RoleModel;
import com.jwt.model.UserModel;
import com.jwt.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

  @Autowired
  UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //TODO User modeli düzelt
    Optional<UserModel> userModel = userRepository.findByUsername(username);
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    for (RoleModel role : userModel.get().getRoles()) {
      authorities.add(new SimpleGrantedAuthority(role.getName()));
    }
    return new User(userModel.get().getUsername(), userModel.get().getPassword(), authorities);
  }

}
