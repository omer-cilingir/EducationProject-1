package com.jwt.model;

import com.jwt.model.base.BaseModel;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "role")
@Data
public class RoleModel extends BaseModel {

  private static final long serialVersionUID = 1L;
  private String name;


}
