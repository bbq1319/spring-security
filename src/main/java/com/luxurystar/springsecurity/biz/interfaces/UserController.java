package com.luxurystar.springsecurity.biz.interfaces;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luxurystar.springsecurity.biz.application.UserService;
import com.luxurystar.springsecurity.biz.domain.user.User;
import com.luxurystar.springsecurity.biz.dto.user.UserRegisterRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	private final BCryptPasswordEncoder encoder;
	private final ModelMapper modelmapper;

	@PostMapping
	public Long register(@RequestBody UserRegisterRequest request) {
		String password = encoder.encode(request.getPassword());
		request.setPassword(password);
		return userService.register(modelmapper.map(request, User.class)).getId();
	}

}
