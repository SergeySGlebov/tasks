package ru.glebov.oauth2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SocialAppService extends DefaultOAuth2UserService {
    private static final Logger logger = LoggerFactory.getLogger(SocialAppService.class);

    private static final String ADMIN_LOGIN = "SergeySGlebov";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            OAuth2User oauth2User = super.loadUser(userRequest);
            logger.info("User authenticated via {}: {}",
                    userRequest.getClientRegistration().getRegistrationId(),
                    oauth2User.getAttributes().get("login"));

            System.out.println("OAuth2User attributes: " + oauth2User.getAttributes());

            Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
            String login = (String) attributes.get("login");

            Set<SimpleGrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority(ROLE_USER));

            if (ADMIN_LOGIN.equalsIgnoreCase(login)) {
                authorities.add(new SimpleGrantedAuthority(ROLE_ADMIN));
                logger.info("Admin user detected: {}", login);
            }

            if (!attributes.containsKey("name")) {
                attributes.put("name", login != null ? login : "Unknown");
            }

            return new DefaultOAuth2User(
                    authorities,
                    attributes,
                    "login" // Имя атрибута, используемого как principal name
            );

        } catch (OAuth2AuthenticationException e) {
            logger.error("OAuth2 authentication failed", e);
            throw e;
        }
    }
}
