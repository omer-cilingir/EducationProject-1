package com.jwt.controller;

import com.jwt.model.RoleModel;
import com.jwt.service.base.RoleService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
public class RoleController {

  @Autowired
  RoleService roleService;

  @PostMapping(path = "/add")
  public ResponseEntity<?> addRole(@Valid @RequestBody RoleModel model) {
    RoleModel existRole = roleService.findByRole(model.getName());
    if (existRole != null) {
      return new ResponseEntity<>("ROLE ALREADY EXISTS", HttpStatus.BAD_REQUEST);
    } else {
      roleService.addRole(model.getName());
      return new ResponseEntity<>(HttpStatus.OK);
    }
  }

  @GetMapping(path = "/all")
  public List<RoleModel> roleList() {
    return roleService.roleList();
  }
}
