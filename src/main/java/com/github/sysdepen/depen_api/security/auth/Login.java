package com.github.sysdepen.depen_api.security.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Login {

	@NotBlank(message = "O CPF/RNE é obrigatório.")
	private String document;

	@NotBlank(message = "A senha é obrigatória.")
	private String password;
	
}
