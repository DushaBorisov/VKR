package com.example.application.backend.service;

import com.example.application.backend.entities.enums.AuthRoles;
import com.example.application.backend.entities.security.Role;
import com.example.application.backend.entities.security.User;
import com.example.application.backend.repositories.RoleRepository;
import com.example.application.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    private final RoleRepository roleRepository;


    public Optional<User> getByUserName(String userName){
        return Optional.ofNullable(userRepository.findByUsername(userName));
    }


    public boolean saveUser(User user, AuthRoles role) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        Role newRole = new Role(role.getRoleName());
        roleRepository.save(newRole);
        user.setRole(newRole);
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

    public void deleteAllUsers(){
        userRepository.deleteAll();
    }
}
