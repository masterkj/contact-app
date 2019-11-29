package com.internetapplication.ws.service.impl;

import com.internetapplication.ws.io.model.Privilege;
import com.internetapplication.ws.io.model.Role;
import com.internetapplication.ws.io.model.UserEntity;
import com.internetapplication.ws.io.repositories.RoleRepository;
import com.internetapplication.ws.io.repositories.UserRepository;
import com.internetapplication.ws.service.UserService;
import com.internetapplication.ws.shared.Utils;
import com.internetapplication.ws.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private MessageSource messages;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public UserDto createUser(UserDto user) {
		UserEntity userEntity = new UserEntity();

		if(userRepository.findByEmail(user.getEmail()) != null) throw new RuntimeException("the user is already existed");
	
		BeanUtils.copyProperties(user, userEntity);
		
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setUserId(utils.generateEntityId());

		userEntity.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));

		UserDto returnedValue = new UserDto();

		BeanUtils.copyProperties(userRepository.save(userEntity), returnedValue);
		
		
		return returnedValue;
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if(userEntity == null)  throw new UsernameNotFoundException(email);

		UserDto returnedValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnedValue);
		return returnedValue;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserDto returnedValue = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(userId);

		if(userEntity == null ) throw new UsernameNotFoundException(userId);

		BeanUtils.copyProperties(userEntity, returnedValue);

		return returnedValue;
	}

//	@Override
//	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//		UserEntity userEntity = userRepository.findByEmail(email);
//		if(userEntity == null)  throw new UsernameNotFoundException(email);
//		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
//	}


	@Override
	public User loadUserByUsername(String email)
			throws UsernameNotFoundException {

		UserEntity user = userRepository.findByEmail(email);
		if (user == null) {
			return new org.springframework.security.core.userdetails.User(
					" ", " ", true, true, true, true,
					getAuthorities(Arrays.asList(
							roleRepository.findByName("ROLE_USER"))));
		}

		return new org.springframework.security.core.userdetails.User(
				user.getEmail(), user.getEncryptedPassword(), getAuthorities(user.getRoles()));
	}

	private List<GrantedAuthority> getAuthorities(
			Collection<Role> roles) {

		return getGrantedAuthorities(getPrivileges(roles));
	}

	private List<String> getPrivileges(Collection<Role> roles) {

		List<String> privileges = new ArrayList<>();
		List<Privilege> collection = new ArrayList<>();
		for (Role role : roles) {
			collection.addAll(role.getPrivileges());
		}
		for (Privilege item : collection) {
			privileges.add(item.getName());
		}
		return privileges;
	}

	private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}
}
