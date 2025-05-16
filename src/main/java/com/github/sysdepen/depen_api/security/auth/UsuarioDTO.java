package com.github.sysdepen.depen_api.security.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String name;
    private String document;
    private String email;
    private String role;
}