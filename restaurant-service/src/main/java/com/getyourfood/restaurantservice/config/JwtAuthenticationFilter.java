package com.getyourfood.restaurantservice.config;

import com.getyourfood.restaurantservice.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("Handling request to: {}", request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("No Bearer token found in Authorization header, continuing chaining");
            filterChain.doFilter(request, response);
            return;
        }
        final String jwtToken = authHeader.substring(7);
        log.info("extracted token: {}", jwtToken);

        try{
            if(!jwtUtil.isTokenValid(jwtToken)){
                log.info("Invalid JWT token received");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or Expired token");
                return;
            }
            String userId = jwtUtil.extractUserId(jwtToken);
            String userRole = jwtUtil.extractRole(jwtToken);
            log.info("Token validated for user ID: {}, role: {}", userId, userRole);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_"+userRole))
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Authentication set in SecurityContext for user: {}, ROle: {}", userId, "ROLE_"+userRole);
        } catch (Exception e) {
            log.error("Failed to process Jwt Token",e);
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token processing failed");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
