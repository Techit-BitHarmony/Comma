package com.bitharmony.comma.global.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        Object exception = request.getAttribute("exception");
        if (exception instanceof ExpiredAccessTokenException) {
            ExpiredAccessTokenException tokenException = (ExpiredAccessTokenException) exception;

            setResponse(response, tokenException);
            return;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    public void setResponse(HttpServletResponse response, ExpiredAccessTokenException ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");

        JSONObject responseJson = new JSONObject();
        responseJson.put("message", ex.getMessage());
        responseJson.put("code", ex.getStatusCode());
        response.getWriter().print(responseJson);
    }
}
