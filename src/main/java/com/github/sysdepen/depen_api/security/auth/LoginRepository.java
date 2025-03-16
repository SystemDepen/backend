package com.github.sysdepen.depen_api.security.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface LoginRepository extends JpaRepository<Usuario, Long>{

	public Optional<Usuario> findByDocument(String login);
	
}
