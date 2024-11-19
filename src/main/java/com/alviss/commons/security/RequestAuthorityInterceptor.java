package com.alviss.commons.security;

import java.util.HashSet;
import java.util.Set;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

public class RequestAuthorityInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(
      final HttpServletRequest request,
      @NonNull final HttpServletResponse response,
      @NonNull final Object handler) {
    Set<GrantedAuthority> authorities = new HashSet<>();
    val principalHeader = request.getHeader("X-User-Id");
    val rolesHeader = request.getHeaders("X-User-Roles");
    if (rolesHeader != null) {
      while (rolesHeader.hasMoreElements()) {
        authorities.add(new SimpleGrantedAuthority(rolesHeader.nextElement()));
      }
    }
    val newAuth = new UsernamePasswordAuthenticationToken(principalHeader, null, authorities);
    SecurityContextHolder.getContext().setAuthentication(newAuth);
    return true;
  }
}
