package com.smart.restaurantAppointment.jwt;

import com.smart.restaurantAppointment.Enumerator.UserRole;
import com.smart.restaurantAppointment.Service.UserDetailService;
import com.smart.restaurantAppointment.security.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }
        try {
            jwt = authHeader.substring(7).trim();
//          retrieve JWT
            Claims claims = jwtService.parseClaims(jwt);
            userEmail = claims.getSubject();
            UserRole role = UserRole.valueOf(claims.get("role", String.class));
            Long  userId = claims.get("userId",Long.class);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (userEmail != null && authentication == null) {
//              Retrieve the stamp from the JWT token
//              init Principal before setting to the spring security object
                AuthenticatedUser principal = new AuthenticatedUser(userId, userEmail,role);
//              Wraps the role into a format Spring security understands
                var authorities = List.of(new SimpleGrantedAuthority(role.name()));
//              create authentication object and set email to principal
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(principal, null , authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//              PreAuthorise will call securityContextHolder
//              SecurityContextholder.getContext -< reads threadLocal
//              .getAuthentication              -< gets the Authentication
//              .getAuthorities()  -<              gets the user role (CUSTOMER) or (MERCHANT)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
//          Set response to 401
            log.warn("Jwt validation failed:{}", exception.getMessage(),exception);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid or expired token\"}");
        }
    }

}