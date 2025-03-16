package com.github.sysdepen.depen_api.services;

import java.util.List;
import java.util.Optional;

import com.github.sysdepen.depen_api.repository.UsuarioRepository;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;


	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	

	public String save (Usuario usuario) {

		usuario.setPassword(this.bCryptPasswordEncoder.encode(usuario.getPassword()));
		this.usuarioRepository.save(usuario);
		return "Usuario cadastrado com sucesso";
	}
	
	public String update (Usuario usuario, long id) {
		usuario.setId(id);
		this.usuarioRepository.save(usuario);
		return "Atualizado com sucesso";
	}
	
	public Usuario findById (long id) {
		
		Optional<Usuario> optional = this.usuarioRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}else
			return null;
		
	}
	
	public List<Usuario> findAll () {
		
		return this.usuarioRepository.findAll();
		
	}
	
	public String delete (long id) {
		this.usuarioRepository.deleteById(id);
		return "Usuário deletado com sucesso!";
	}
	
	
}
