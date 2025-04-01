package ru.glebov.jwt.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.glebov.jwt.dto.JwtRequest;
import ru.glebov.jwt.dto.JwtResponse;
import ru.glebov.jwt.model.Role;
import ru.glebov.jwt.model.User;
import ru.glebov.jwt.repository.UserRepository;
import ru.glebov.jwt.utils.JWTUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthLogService authLogService;

    public AuthService(UserRepository userRepository, JWTUtils jwtUtils, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, AuthLogService authLogService) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.authLogService = authLogService;
    }

    public JwtResponse signUp(JwtRequest registrationRequest) {
        JwtResponse resp;
        try {
            User user = new User();
            user.setEmail(registrationRequest.login());
            user.setPassword(passwordEncoder.encode(registrationRequest.password()));
            Set<Role> roles = new HashSet<>();
            for (String role : registrationRequest.roles()) {
                roles.add(Role.valueOf(role));
            }
            user.setRoles(roles);
            userRepository.save(user);
            authLogService.registration(user.getUsername(), "");
            resp = new JwtResponse(200, "User Saved Successfully", "", "");
        } catch (Exception e) {
            e.printStackTrace();
            resp = new JwtResponse(500, e.getMessage(), "", "");
        }
        return resp;
    }

    public JwtResponse signIn(JwtRequest signinRequest) {
        JwtResponse resp;
        User user = userRepository.findByEmail(signinRequest.login()).orElseThrow();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.login(), signinRequest.password()));
            System.out.println("USER IS: " + user);
            String jwt = jwtUtils.generateToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            user.setFailedAttempts(0);
            userRepository.save(user);
            authLogService.logSuccess(user.getUsername(), jwt);
            resp = new JwtResponse(200, "Successfully Signed In", jwt, refreshToken);
        } catch (BadCredentialsException e) {
            authLogService.logFailure(user.getUsername(), "Invalid credentials");
            user.setFailedAttempts(user.getFailedAttempts() + 1);
            if (user.getFailedAttempts() > 5) {
                user.setBlocked(true);
                authLogService.logAccountLocked(user.getUsername());
            }
            userRepository.save(user);
            resp = new JwtResponse(401, e.getMessage(), "", "");
        } catch (LockedException e) {
            authLogService.logFailure(user.getUsername(), "Account locked");
            resp = new JwtResponse(401, e.getMessage(), "", "");
        }catch (Exception e) {
            e.printStackTrace();
            resp = new JwtResponse(500, e.getMessage(), "", "");
        }
        return resp;
    }

    public JwtResponse refreshToken(JwtRequest jwtRequest) {
        JwtResponse resp;
        String email = jwtUtils.extractUsername(jwtRequest.refreshToken());
        User user = userRepository.findByEmail(email).orElseThrow();
        if (jwtUtils.isTokenValid(jwtRequest.refreshToken(), user)) {
            var jwt = jwtUtils.generateToken(user);
            authLogService.logTokenRefresh(user.getUsername(), jwt);
            resp = new JwtResponse(200, "Successfully Refreshed Token", jwt, "");
        } else {
            resp = new JwtResponse(500, "Wrong Token", "", "");
        }
        return resp;
    }

    public boolean unblockUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        if (user.isAccountNonLocked()) {
            return false;
        }
        user.setBlocked(false);
        user.setFailedAttempts(0);
        userRepository.save(user);
        return true;
    }
}
