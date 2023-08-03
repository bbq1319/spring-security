package com.luxurystar.springsecurity.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.luxurystar.springsecurity.biz.domain.user.User;
import com.luxurystar.springsecurity.biz.domain.user.UserRepository;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	@Value("${jwt.secret-key}")
	private String secretKey;

	private final UserRepository userRepository;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		String authorization = "Authorization";
		String bearer = "Bearer";
		String header = request.getHeader(authorization);

		if (header == null || !header.startsWith(bearer)) {
			chain.doFilter(request, response);
			return;
		}
		String token = request.getHeader(authorization).replace(bearer, "");
		String username = Jwts.parser()
			.setSigningKey(secretKey)
			.parseClaimsJws(token)
			.getBody()
			.getSubject();

		if (username != null) {
			User user = userRepository.findByUsername(username).orElseThrow();
			Authentication authentication =
				new UsernamePasswordAuthenticationToken(
					user,
					null, // 패스워드는 모르니까 null 처리, 인증 용도 x
					user.getAuthorities());

			// 권한 관리를 위해 세션에 접근하여 값 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		chain.doFilter(request, response);
	}

}
