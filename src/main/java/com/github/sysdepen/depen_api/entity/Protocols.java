package com.github.sysdepen.depen_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.persistence.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "tb_protocols")
public class Protocols {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime updated_at;

    @ManyToOne
    @JsonIgnoreProperties("protocols")
    private Usuario user;

    @OneToOne
    @JsonIgnoreProperties
    private RequerimentoInfo req_info;

    @OneToOne
    @JsonIgnoreProperties
    private Documents doc;

    @ManyToOne
    @JsonIgnoreProperties
    private Admin admin;

    // STATUS DO PROTOCOLO!!
    // 1 = PENDENTE
    // 2 =  ANALISE
    // 3 = EMITIDO
    // 4 = RECUSADO
    @NotNull
    @Column (nullable = false)
    private Long status;
}
