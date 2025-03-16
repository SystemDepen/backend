package com.github.sysdepen.depen_api.security.auth;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

	@Autowired
	private LoginService loginService;

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
