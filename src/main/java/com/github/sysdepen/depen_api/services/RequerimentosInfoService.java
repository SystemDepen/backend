package com.github.sysdepen.depen_api.services;

import java.util.List;
import java.util.Optional;

import com.github.sysdepen.depen_api.entity.Protocols;
import com.github.sysdepen.depen_api.entity.RequerimentoInfo;
import com.github.sysdepen.depen_api.repository.RequerimentosInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RequerimentosInfoService{

    @Autowired
    private RequerimentosInfoRepository requerimentosInfoRepository;


    public RequerimentoInfo save(RequerimentoInfo requerimentoInfo) {
        return requerimentosInfoRepository.save(requerimentoInfo);
    }

    public List<RequerimentoInfo> findAll() {
        return requerimentosInfoRepository.findAll();
    }

    public Optional<RequerimentoInfo> findById(Long id) {
        return requerimentosInfoRepository.findById(id);
    }

    public RequerimentoInfo update(RequerimentoInfo requerimentoInfo) {
        return requerimentosInfoRepository.save(requerimentoInfo);
    }

    public boolean deleteById(Long id) {
        if (requerimentosInfoRepository.existsById(id)) {
            requerimentosInfoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
