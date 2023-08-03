package com.luxurystar.springsecurity.security;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxurystar.springsecurity.biz.domain.user.User;
import com.luxurystar.springsecurity.biz.dto.user.LoginRequest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	// 인증 요청시에 실행되는 함수 => /login
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
		throws AuthenticationException {
		log.info("JwtAuthenticationFilter : 진입");

		// request에 있는 username과 password를 파싱해서 자바 Object로 받기
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
		LoginRequest loginRequest = null;
		try {
			InputStream inputStream = request.getInputStream();
			String message = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
			loginRequest = objectMapper.readValue(message, LoginRequest.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// TODO
		if (loginRequest == null) {
			return null;
		}

		// 유저네임패스워드 토큰 생성
		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(
				loginRequest.getUsername(),
				loginRequest.getPassword());

		return authenticationManager.authenticate(authenticationToken);
	}

	// JWT Token 생성해서 response에 담아주기
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) {
		User user = (User)authResult.getPrincipal();
		String token = Jwts.builder()
			.setSubject(user.getUsername())
			.setExpiration(new Date(System.currentTimeMillis() + JwtProperties.VALIDATE_TIME))
			.signWith(SignatureAlgorithm.HS512, JwtProperties.SECRET_KEY)
			.compact();
		response.addHeader("token", token);
	}

}
