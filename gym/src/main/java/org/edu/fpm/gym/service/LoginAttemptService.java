package org.edu.fpm.gym.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {
    private final int MAX_ATTEMPTS = 3;
    private final long BLOCK_TIME_MS = TimeUnit.MINUTES.toMillis(5);

    private final ConcurrentHashMap<String, Integer> attempts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> blockedUsers = new ConcurrentHashMap<>();

    public void loginFailed(String username) {
        attempts.merge(username, 1, Integer::sum);
        if (attempts.get(username) >= MAX_ATTEMPTS) {
            blockedUsers.put(username, System.currentTimeMillis());
        }
    }

    public boolean isBlocked(String username) {
        if (!blockedUsers.containsKey(username)) return false;
        if (System.currentTimeMillis() - blockedUsers.get(username) > BLOCK_TIME_MS) {
            blockedUsers.remove(username);
            attempts.remove(username);
            return false;
        }
        return true;
    }

    public void loginSucceeded(String username) {
        attempts.remove(username);
        blockedUsers.remove(username);
    }
}
