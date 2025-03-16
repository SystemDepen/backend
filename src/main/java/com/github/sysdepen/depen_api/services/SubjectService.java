package com.github.sysdepen.depen_api.services;

import java.util.List;
import java.util.Optional;

import com.github.sysdepen.depen_api.entity.Subject;
import com.github.sysdepen.depen_api.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class SubjectService {
	 @Autowired
	    private SubjectRepository subjectRepository;

	    public Subject save(Subject subject) {
	        return subjectRepository.save(subject);
	    }

	    public List<Subject> findAll() {
	        return subjectRepository.findAll();
	    }

	    public Optional<Subject> findById(Long id) {
	        return subjectRepository.findById(id);
	    }

	    public Subject update(Subject subject) {
	        return subjectRepository.save(subject);
	    }

	    public void deleteById(Long id) {
	    	subjectRepository.deleteById(id);
	    }
}