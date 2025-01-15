package com.fastcampus.board.config;

import com.fastcampus.board.exception.jwt.JwtTokenNotFoundException;
import com.fastcampus.board.service.JwtService;
import com.fastcampus.board.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String BearerPrefix = "Bearer ";
        var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        var securityContext = SecurityContextHolder.getContext();

        // JWT 토큰이 존재하지 않거나 Bearer 로 시작하지 않는 경우 예외 발생
        if (ObjectUtils.isEmpty(authorization) || !authorization.startsWith(BearerPrefix)) {
            throw new JwtTokenNotFoundException();
        }

        // JWT 검증
        if (!ObjectUtils.isEmpty(authorization)
                && authorization.startsWith(BearerPrefix)
                && securityContext.getAuthentication() == null) {
            // Authorization 헤더에서 토큰 값만 추출
            var accessToken = authorization.substring(BearerPrefix.length());
            // accessToken 값으로 사용자 명 추출
            String username = jwtService.getUsername(accessToken);
            // 사용자 명으로 사용자 정보 조회
            var userDetails = userService.loadUserByUsername(username);

            // 시큐리티 컨텍스트에 설정
            var authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);
        }

        filterChain.doFilter(request, response);
    }
}
