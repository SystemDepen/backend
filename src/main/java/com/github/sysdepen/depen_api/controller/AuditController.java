package com.github.sysdepen.depen_api.controller;


import com.github.sysdepen.depen_api.audit.AuditHistoryDTO;
import com.github.sysdepen.depen_api.audit.CustomRevisionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/audit")
@CrossOrigin(origins = "*")
public class AuditController {

  @PersistenceContext
  private EntityManager entityManager;

  @GetMapping("/history/{entityName}/{id}")
  public ResponseEntity<?> getEntityHistory(@PathVariable String entityName, @PathVariable Long id) {
    AuditReader auditReader = AuditReaderFactory.get(entityManager);
    Class<?> entityClass;

    try {
      String fullClassName = entityName.equals("Usuario")
        ? "com.github.sysdepen.depen_api.security.auth." + entityName
        : "com.github.sysdepen.depen_api.entity." + entityName;
      entityClass = Class.forName(fullClassName);
    } catch (ClassNotFoundException e) {
      return ResponseEntity.badRequest().body("{\"error\":\"Entidade '" + entityName + "' não encontrada.\"}");
    }

    AuditQuery query = auditReader.createQuery()
      .forRevisionsOfEntity(entityClass, false, true)
      .add(org.hibernate.envers.query.AuditEntity.id().eq(id));

    @SuppressWarnings("unchecked")
    List<Object[]> result = query.getResultList();
    List<AuditHistoryDTO> history = new ArrayList<>();

    for (Object[] resultItem : result) {
      Object entitySnapshot = resultItem[0];
      CustomRevisionEntity revision = (CustomRevisionEntity) resultItem[1];
      org.hibernate.envers.RevisionType revisionType = (org.hibernate.envers.RevisionType) resultItem[2];

      history.add(new AuditHistoryDTO(
        revision.getId(),
        revision.getUsername(),
        new Date(revision.getTimestamp()),
        revisionType,
        entitySnapshot
      ));
    }
    return ResponseEntity.ok(history);
  }


}
