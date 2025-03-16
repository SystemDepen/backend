package com.github.sysdepen.depen_api.services;

import java.util.List;
import java.util.Optional;


import com.github.sysdepen.depen_api.entity.SubjectInmostVisit;
import com.github.sysdepen.depen_api.repository.SubInMostVisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class SubInMostVisitService{

        @Autowired
    private SubInMostVisitRepository subInMostVisitRepository;

    public SubjectInmostVisit save(SubjectInmostVisit subInMostVisit) {
        return subInMostVisitRepository.save(subInMostVisit);
    }

    public List<SubjectInmostVisit> findAll() {
        return subInMostVisitRepository.findAll();
    }

    public Optional<SubjectInmostVisit> findById(Long id) {
        return subInMostVisitRepository.findById(id);
    }

    public SubjectInmostVisit update(SubjectInmostVisit subInMostVisit) {
        return subInMostVisitRepository.save(subInMostVisit);
    }

    public void deleteById(Long id) {
    	subInMostVisitRepository.deleteById(id);
    }
}