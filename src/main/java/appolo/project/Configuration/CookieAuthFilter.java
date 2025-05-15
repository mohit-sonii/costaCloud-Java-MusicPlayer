package appolo.project.Configuration;


import appolo.project.Util.TokenCookie;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Configuration
public class CookieAuthFilter extends OncePerRequestFilter {
    @Autowired
    private TokenCookie tokenCookie;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/join")) {
            filterChain.doFilter(request, response);
            return;
        }
        Cookie[] cookies = request.getCookies();
        boolean foundCooki = false;
        for (Cookie item : cookies) {
            if ("auth_for_sec".equals(item.getName())) {
                String[] result = tokenCookie.tokenValidation(item.getValue(), request.getRequestURI().startsWith("/u") ? "USER" : "ADMIN");
                if (result[0].equals("false")) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                try {
                    Claims claims = tokenCookie.getClaims(item.getValue(), request.getRequestURI().startsWith("/u") ? "USER" : "ADMIN");
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(claims.getSubject(), null, Collections.singleton(new SimpleGrantedAuthority(request.getRequestURI().startsWith("/u") ? "USER" : "ADMIN")));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    foundCooki = true;
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token parsing failed");
                    return;
                }
                break;
            }
        }
        if (!foundCooki && !request.getRequestURI().startsWith("/join")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
