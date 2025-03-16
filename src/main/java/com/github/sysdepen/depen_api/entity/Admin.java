package com.github.sysdepen.depen_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "tb_admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 256)
    @NotBlank(message = "Nome não pode estar em branco.")
    private String name;

    @Column(length = 14)
    @Pattern(
            regexp = "([0-9]{3}\\.[0-9]{3}\\.[0-9]{3}\\-[0-9]{2}|[0-9]{11})|([a-zA-Z0-9]{1,12})",
            message = "Documento inválido: forneça um CPF válido (com ou sem pontuação) ou um RNE válido"
    )
    private String document;

    @Column(length = 256)
    @Email(message = "E-mail inválido.")
    @NotBlank
    private String email;

    @Column(nullable = false, length = 256)
    @NotBlank(message = "Senha não pode estar em branco.")
    private String password;

    @Column(nullable = false)
    private Short role;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime updated_at;

    @OneToMany
    @JsonIgnoreProperties
    private List<Protocols> protocols;
}
