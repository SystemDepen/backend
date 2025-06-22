package com.github.sysdepen.depen_api.audit;

import com.github.sysdepen.depen_api.audit.CustomRevisionListener;
import jakarta.persistence.*;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import java.io.Serializable;
import lombok.Data;

@Entity
@RevisionEntity(CustomRevisionListener.class)
@Table(name = "revinfo")
@Data
public class CustomRevisionEntity implements Serializable {

  @Id
  @RevisionNumber
  @GeneratedValue
  private int id;

  @RevisionTimestamp
  private long timestamp;

  private String username;

}
