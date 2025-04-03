package ru.glebov.oauth2.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("error", "Доступ запрещен");
                model.addAttribute("message", "У вас нет прав для просмотра этой страницы");
                return "error";
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return "redirect:/login";
            }
        }

        model.addAttribute("error", "Ошибка");
        model.addAttribute("message", "Произошла непредвиденная ошибка");
        return "error";
    }

    @RequestMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("error", "403 - Доступ запрещен");
        model.addAttribute("message", "У вас недостаточно прав для этого действия");
        return "error";
    }
}