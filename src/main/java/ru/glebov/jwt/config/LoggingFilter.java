package ru.glebov.jwt.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Map;

@Component
public class LoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            if (request.getRequestURI().contains("/auth/")) {
                logAuthRequest(request, response, duration);
            }

            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logAuthRequest(HttpServletRequest request,
                                HttpServletResponse response,
                                long duration) {
        String username = "unknown";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> requestBody = objectMapper.readValue(
                    request.getInputStream(),
                    new TypeReference<>() {});

            if (requestBody.containsKey("login")) {
                username = requestBody.get("login").toString();
            }
        } catch (IOException e) {
            // Если не удалось прочитать JSON, оставляем username как "unknown"
            logger.debug("Failed to parse request body as JSON", e);
        }

        String logMessage = String.format(
                "AUTH_ATTEMPT | IP: %s | Method: %s | URI: %s | User: %s | Status: %d | Duration: %dms",
                request.getRemoteAddr(),
                request.getMethod(),
                request.getRequestURI(),
                username,
                response.getStatus(),
                duration
        );

        if (response.getStatus() >= 200 && response.getStatus() < 300) {
            logger.info(logMessage);
        } else {
            logger.warn(logMessage);
        }
    }
}
