package com.github.sysdepen.depen_api.security.auth;

import com.github.sysdepen.depen_api.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

@RestController
@RequestMapping("/v1/login")
@CrossOrigin(origins = "*")
public class LoginController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private UsuarioRepository repository;

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@PostMapping("/logar")
	public ResponseEntity<String> logar(@Valid @RequestBody Login login) {
		try {
			String token = loginService.logar(login);
			return new ResponseEntity<>(token, HttpStatus.OK);
		} catch (AuthenticationException e) {
			return new ResponseEntity<>("Falha na autenticação: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			return new ResponseEntity<>("Erro ao realizar login: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}


}
