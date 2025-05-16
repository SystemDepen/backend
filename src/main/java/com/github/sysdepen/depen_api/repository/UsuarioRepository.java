package com.github.sysdepen.depen_api.repository;

import java.util.List;
import java.util.Optional;

import com.github.sysdepen.depen_api.security.auth.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByDocument(String document);

}
