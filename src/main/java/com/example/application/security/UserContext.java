package com.example.application.security;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;

@Component
public class UserContext {

    private HashMap<String, UserData> authUsersMap = new HashMap<>();

    public boolean isAuthorised(String sessionId) {
        return authUsersMap.containsKey(sessionId);
    }

    public Optional<UserData> getAuthenticatedUser(String sessionId) {
        if (authUsersMap.containsKey(sessionId))
            return Optional.ofNullable(authUsersMap.get(sessionId));
        else return Optional.empty();
    }

    public void addUserToContext(String sessionId, UserData userData) {
        authUsersMap.put(sessionId, userData);
    }

    public void deleteUserFromContext(String sessionId) {
        authUsersMap.remove(sessionId);
    }


}
