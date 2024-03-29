package com.internetapplication.ws.service;

import com.internetapplication.ws.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto user);

	UserDto getUser(String email);

    UserDto getUserByUserId(String userId);
}
