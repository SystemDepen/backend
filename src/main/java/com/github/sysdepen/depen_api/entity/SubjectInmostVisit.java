package com.github.sysdepen.depen_api.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "tb_subject_inmost_visit")
@Audited
@EntityListeners(AuditingEntityListener.class)
public class SubjectInmostVisit {

    // Regex pattern para tempo de gravidez (1 a 10 meses)
    public static final String GRAVIDEZ_REGEX = "^(0?[1-9]|10)$";
    public static final String ACQUISITION_REGEX = "^(sim|não)$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = ACQUISITION_REGEX, message = "O valor deve ser 'sim' ou 'não'.")
    private String accomplice;

    @NotNull
    @Column(nullable = false)
    private Boolean victim;

    @NotNull
    @Column(nullable = false)
    private Boolean pregnancy;

    @NotNull
    @Pattern(regexp = GRAVIDEZ_REGEX, message = "O tempo de gravidez deve estar entre 1 e 10 meses. Mais que isso você ta plantando soja na barriga")
    private String time_pregnancy;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updated_at;
}
