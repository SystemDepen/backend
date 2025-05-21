//AuthenticationService.java
package com.github.sysdepen.depen_api.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.core.AuthenticationException;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@Service
public class LoginService {
	
	@Autowired
	private LoginRepository repository;


	private static final String TOKEN_URL = "http://backend.local.sysdepen.com.br:8433/realms/projeto-mensal/protocol/openid-connect/token";
	private static final String CLIENT_ID = "backend-depen";
	private static final String CLIENT_SECRET= "RJPm5lBOmA2q86G4eliBzRiv1MgBAsbj";
	private final RestTemplate restTemplate = new RestTemplate();

	public String logar(Login login) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("grant_type", "password");
		form.add("client_id", CLIENT_ID);
		form.add("client_secret", CLIENT_SECRET);
		form.add("username", login.getDocument());
		form.add("password", login.getPassword());

		HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(form, headers);

		try {
			ResponseEntity<String> response = restTemplate.postForEntity(TOKEN_URL, request, String.class);
			return response.getBody();
		} catch (HttpClientErrorException e) {
			// repassa a mensagem de erro do Keycloak
			throw new RuntimeException("Falha ao autenticar no Keycloak: " + e.getResponseBodyAsString());
		}
	}
}