package com.luxurystar.springsecurity.biz.dto.user;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest {

	private String username;
	private String password;
	private String name;
	List<String> roles;

}
