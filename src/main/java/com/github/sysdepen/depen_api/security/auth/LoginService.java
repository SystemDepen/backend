//AuthenticationService.java
package com.github.sysdepen.depen_api.security.auth;

import com.github.sysdepen.depen_api.security.config.JwtServiceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.core.AuthenticationException;

@Service
public class LoginService {
	
	@Autowired
	private LoginRepository repository;

	@Autowired
	private JwtServiceGenerator jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	public String logar(Login login) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							login.getDocument(),
							login.getPassword()
					)
			);
			Usuario user = repository.findByDocument(login.getDocument()).orElseThrow();
			return jwtService.generateToken(user);
		} catch (BadCredentialsException e) {
			throw new AuthenticationException("Usuário ou senha incorretos.") {};
		} catch (AuthenticationException e) {
			throw new AuthenticationException("Falha na autenticação.") {};
		}

	}
}