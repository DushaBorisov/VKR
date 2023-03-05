package com.example.application.backend.service;

import com.example.application.backend.entities.enums.AuthRoles;
import com.example.application.backend.entities.security.Role;
import com.example.application.backend.entities.security.User;
import com.example.application.backend.repositories.RoleRepository;
import com.example.application.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public boolean saveUser(User user, AuthRoles role) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        Role newRole = new Role(role.getRoleName());
        roleRepository.save(newRole);
        user.setRole(newRole);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public void saveStudent(User student) {
        saveUser(student, AuthRoles.ROLE_USER);
    }

    public void saveCompany(User student) {
        saveUser(student, AuthRoles.ROLE_COMPANY);
    }

    public void saveAdmin(User student) {
        saveUser(student, AuthRoles.ROLE_ADMIN);
    }
}
