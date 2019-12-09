package com.internetapplication.ws.security;

import com.google.gson.Gson;
import com.internetapplication.ws.SpringApplicationContext;
import com.internetapplication.ws.service.UserService;
import com.internetapplication.ws.shared.dto.UserDto;
import com.internetapplication.ws.ui.model.request.UserLoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.internetapplication.ws.ui.model.response.LoginResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
		
	@Override
	public Authentication attemptAuthentication (HttpServletRequest req, HttpServletResponse res) {
		try {
			UserLoginRequestModel creds = new ObjectMapper()
					.readValue(req.getInputStream(), UserLoginRequestModel.class);
			
			
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getEmail(),
							creds.getPassword(),
							new ArrayList<>()
							)
					);
		} catch (IOException e ) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest req,
											HttpServletResponse res,
											FilterChain chain,
											Authentication auth) throws IOException, ServletException {
		String userName = ((User) auth.getPrincipal()).getUsername();
		
		String token = Jwts.builder()
				.setSubject(userName)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstance.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstance.getTokenSecret())
				.compact();

		UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");
		UserDto userDto = userService.getUser(userName);

		PrintWriter out = res.getWriter();
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");


		String accessToken = SecurityConstance.TOKEN_PREFIX + token;
		String loginResponseString = new Gson().toJson(new LoginResponse(token, userDto.getUserId()) );
		out.print(loginResponseString);
		out.flush();
				
//		res.addHeader(SecurityConstance.HEADER_STRING, SecurityConstance.TOKEN_PREFIX + token);
//		res.addHeader("UserID", userDto.getUserId());

	}

	
}
