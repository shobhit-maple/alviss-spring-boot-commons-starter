package com.alviss.commons.security;

import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
public class SecurityService {

  public boolean hasPermission(final String role){
    val auth = SecurityContextHolder.getContext().getAuthentication();
    val permissions = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    return permissions.contains("ROLE_GLOBAL_ADMIN") || permissions.contains(role);
  }
}