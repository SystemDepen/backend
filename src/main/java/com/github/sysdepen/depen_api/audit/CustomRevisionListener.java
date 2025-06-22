package com.github.sysdepen.depen_api.audit;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class CustomRevisionListener implements RevisionListener {

  @Override
  public void newRevision(Object revisionEntity) {
    CustomRevisionEntity customRevisionEntity = (CustomRevisionEntity) revisionEntity;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      customRevisionEntity.setUsername("ANONYMOUS");
      return;
    }

    Object principal = authentication.getPrincipal();
    if (principal instanceof Jwt) {
      Jwt jwt = (Jwt) principal;
      customRevisionEntity.setUsername(jwt.getClaimAsString("preferred_username"));
    } else {
      customRevisionEntity.setUsername(authentication.getName());
    }
  }
}
