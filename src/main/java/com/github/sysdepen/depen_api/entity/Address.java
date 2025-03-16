package com.github.sysdepen.depen_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_address")
public class Address {
    // Regex pattern para UF
    public static final String UF_REGEX = "^(AC|AL|AP|AM|BA|CE|DF|ES|GO|MA|MT|MS|MG|PA|PB|PR|PE|PI|RJ|RN|RS|RO|RR|SC|SP|SE|TO)$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 9)
    @Pattern(regexp = "^[0-9]{5}-[0-9]{3}$", message = "CEP deve estar no formato 00000-000")
    private String cep;

    @Column(nullable = false, length = 256)
    @NotBlank(message = "O país não pode estar em branco.")
    @Size(max = 256, message = "O país deve ter no máximo 256 caracteres.")
    private String country;

    @Column(nullable = false, length = 2)
    @Pattern(regexp = UF_REGEX, message = "UF inválida. Deve ser uma sigla de UF válida do Brasil.")
    private String UF;

    @Column(nullable = false, length = 256)
    @NotBlank(message = "A cidade não pode estar em branco.")
    @Size(max = 256, message = "A cidade deve ter no máximo 256 caracteres.")
    private String city;

    @Column(nullable = false, length = 256)
    @NotBlank(message = "O bairro não pode estar em branco.")
    @Size(max = 256, message = "O bairro deve ter no máximo 256 caracteres.")
    private String district;

    @Column(nullable = false, length = 256)
    @NotBlank(message = "A rua não pode estar em branco.")
    @Size(max = 256, message = "A rua deve ter no máximo 256 caracteres.")
    private String street;

    @Column(nullable = false, length = 256)
    @NotBlank(message = "O número da casa não pode estar em branco.")
    @Size(max = 256, message = "O número da casa deve ter no máximo 256 caracteres.")
    private String number_house;

    @OneToOne
    @JsonIgnoreProperties
    private Usuario user;
}

