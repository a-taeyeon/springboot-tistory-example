package com.tistory.framework.security.handler;

import com.tistory.framework.core.response.BaseResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 *     접근 거부 처리 핸들러를 Bean으로 정의합니다.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 접근 거부 시 403 상태 코드와 에러 메시지를 반환합니다.
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("Access Denied: You do not have the necessary permissions to access this resource.");
    }
}