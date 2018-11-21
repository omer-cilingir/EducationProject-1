package com.jwt.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final String AUTHORIZATION_HEADER = "X-Auth-Token";

  //TODO
  @Autowired
  private TokenProvider tokenProvider;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = resolveToken(request);
      if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
        Authentication authentication = tokenProvider.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
      filterChain.doFilter(request, response);
    } catch (ExpiredJwtException eje) {
      log.info("Security exception for user {} - {}", eje.getClaims().getSubject(), eje.getMessage());
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  private String resolveToken(HttpServletRequest request) {
    String authToken = request.getHeader(AUTHORIZATION_HEADER);
    return StringUtils.hasText(authToken) ? authToken : null;
  }
}
