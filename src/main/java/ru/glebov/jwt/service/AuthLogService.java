package ru.glebov.jwt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.glebov.jwt.model.User;
import ru.glebov.jwt.repository.UserRepository;

@Service
public class AuthLogService {
    private static final Logger logger = LoggerFactory.getLogger(AuthLogService.class);

    private final UserRepository userRepository;

    public AuthLogService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void logSuccess(String username, String token) {
        User user = userRepository.findByEmail(username)
                .orElse(null);

        String logMessage = String.format(
                "AUTH_SUCCESS | User: %s | UserID: %s | Token: %s",
                username,
                user != null ? user.getId() : "unknown",
                maskToken(token)
        );

        logger.info(logMessage);
    }

    public void registration(String username, String token) {
        User user = userRepository.findByEmail(username)
                .orElse(null);

        String logMessage = String.format(
                "REGISTRATION | User: %s | UserID: %s | Token: %s",
                username,
                user != null ? user.getId() : "unknown",
                maskToken(token)
        );

        logger.info(logMessage);
    }

    public void logFailure(String username, String reason) {
        String logMessage = String.format(
                "AUTH_FAILURE | User: %s | Reason: %s",
                username != null ? username : "unknown",
                reason
        );

        logger.warn(logMessage);
    }

    public void logAccountLocked(String username) {
        String logMessage = String.format(
                "ACCOUNT_LOCKED | User: %s",
                username
        );

        logger.error(logMessage);
    }

    public void logTokenRefresh(String username, String newToken) {
        String logMessage = String.format(
                "TOKEN_REFRESH | User: %s | NewToken: %s",
                username,
                maskToken(newToken)
        );

        logger.info(logMessage);
    }

    private String maskToken(String token) {
        if (token == null || token.length() < 10) {
            return "invalid_token";
        }
        return token.substring(0, 5) + "..." + token.substring(token.length() - 5);
    }
}
