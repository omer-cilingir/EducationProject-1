package com.jwt.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jwt.model.base.BaseModel;

@Entity
@Table(name = "role")
public class RoleModel extends BaseModel {

	private static final long serialVersionUID = 1L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
