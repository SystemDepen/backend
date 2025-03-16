package com.github.sysdepen.depen_api.services;

import java.util.List;
import java.util.Optional;

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

    public RequerimentoInfo update(Long id, RequerimentoInfo reqUpdated) {
        Optional<RequerimentoInfo> currentReq = requerimentosInfoRepository.findById(id);

        if (currentReq.isPresent()) {
            RequerimentoInfo req = currentReq.get();
            req.setName_visited(reqUpdated.getName_visited() != null ? reqUpdated.getName_visited() : req.getName_visited());
            req.setCpf_rne(reqUpdated.getCpf_rne() != null ? reqUpdated.getCpf_rne() : req.getCpf_rne());
            req.setSubject(reqUpdated.getSubject() != null ? reqUpdated.getSubject() : req.getSubject());
            req.setType_visitation(reqUpdated.getType_visitation() != null ? reqUpdated.getType_visitation() : req.getType_visitation());
            req.setState(reqUpdated.getState() != null ? reqUpdated.getState() : req.getState());
            req.setCity(reqUpdated.getCity() != null ? reqUpdated.getCity() : req.getCity());
            req.setDistrict(reqUpdated.getDistrict() != null ? reqUpdated.getDistrict() : req.getDistrict());
            req.setStreet(reqUpdated.getStreet() != null ? reqUpdated.getStreet() : req.getStreet());
            req.setNumber_house(reqUpdated.getNumber_house() != null ? reqUpdated.getNumber_house() : req.getNumber_house());
            return requerimentosInfoRepository.save(req);
        } else {
            throw new RuntimeException("Req not found with id " + id);
        }
    }

    public void deleteById(Long id) {
        requerimentosInfoRepository.deleteById(id);
    }
}
