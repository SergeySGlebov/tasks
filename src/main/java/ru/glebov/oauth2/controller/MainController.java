package ru.glebov.oauth2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;
import java.util.Optional;

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("loginPage", true);
        return "index"; // index.html в templates/
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/login-success")
    public String loginSuccess(@AuthenticationPrincipal OAuth2User principal) {
        logger.info("User {} successfully logged in", Optional.ofNullable(principal.getAttribute("login")));
        return "redirect:/user";
    }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null) {
            return "redirect:/login"; // Перенаправление, если пользователь не аутентифицирован
        }

        // Безопасное добавление атрибутов
        model.addAttribute("name", principal.getAttribute("name") != null ?
                principal.getAttribute("name") : "Не указано");
        model.addAttribute("login", principal.getAttribute("login") != null ?
                principal.getAttribute("login") : "Не указано");
        model.addAttribute("id", principal.getAttribute("id") != null ?
                Objects.requireNonNull(principal.getAttribute("id")).toString() : "Не указано");
        model.addAttribute("email", principal.getAttribute("email") != null ?
                principal.getAttribute("email") : "Не указано");

        return "user";
    }
}
