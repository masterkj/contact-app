package com.internetapplication.ws;

import com.internetapplication.ws.io.model.Privilege;
import com.internetapplication.ws.io.model.Role;
import com.internetapplication.ws.io.model.UserEntity;
import com.internetapplication.ws.io.repositories.PrivilegeRepository;
import com.internetapplication.ws.io.repositories.RoleRepository;
import com.internetapplication.ws.io.repositories.UserRepository;
import com.internetapplication.ws.shared.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;

@Component
public class InitialDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        Collection<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        UserEntity user = new UserEntity();
        user.setFirstName("Admin");
        user.setLastName("Admin");
        user.setEncryptedPassword(bCryptPasswordEncoder.encode("12345678"));
        user.setUserId(utils.generateEntityId());

        user.setEmail("admin@admin.com");
        user.setRoles(Arrays.asList(adminRole));
        userRepository.save(user);

        alreadySetup = true;
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}