package com.github.sysdepen.depen_api.services;

import java.util.List;
import java.util.Optional;

import com.github.sysdepen.depen_api.entity.Protocols;
import com.github.sysdepen.depen_api.repository.ProtocoloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProtocoloService {
	@Autowired
    private ProtocoloRepository protocoloRepository;


    public Protocols save(Protocols protocols) {
        return protocoloRepository.save(protocols);
    }



    public List<Protocols>findAll() {
        return protocoloRepository.findAll();
    }


    public Optional<Protocols> findById(Long id) {
        return protocoloRepository.findById(id);
    }



    public Protocols update(Protocols protocols) {
        return protocoloRepository.save(protocols);
    }


    public void deleteById(Long id) {
    	protocoloRepository.deleteById(id);
    }
}
