package com.github.sysdepen.depen_api.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.envers.RevisionType;
import java.util.Date;


@Data
@AllArgsConstructor
public class AuditHistoryDTO {

  private int revisionId;
  private String author;
  private Date revisionDate;
  private RevisionType revisionType;
  private Object entity;
}
