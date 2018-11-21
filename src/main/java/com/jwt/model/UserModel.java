package com.jwt.model;

import com.jwt.model.base.BaseModel;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class UserModel extends BaseModel {

  private static final long serialVersionUID = 1L;
  private String username;
  private String password;
  private String email;
  private String activationKey;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_role", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id",
      "role_id"}), joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Set<RoleModel> roles = new HashSet<>(0);


}
