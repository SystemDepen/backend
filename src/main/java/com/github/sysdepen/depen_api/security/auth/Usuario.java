package com.github.sysdepen.depen_api.security.auth;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.sysdepen.depen_api.entity.Protocols;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@Table(name = "tb_usuario")
public class Usuario {
	
	//private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String KeyCloakId;

	@NotBlank(message = "O nome é obrigatório.")
	private String name;

	@NotBlank(message = "O documento é obrigatório.")
	private String document;

//	@Transient
//	private String password;

	@Email(message = "E-mail inválido.")
	private String email;

	private String role;
	private LocalDate date_born;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;

	@OneToMany(mappedBy = "user")
	@JsonIgnoreProperties("user")
	private List<Protocols> protocols;


}