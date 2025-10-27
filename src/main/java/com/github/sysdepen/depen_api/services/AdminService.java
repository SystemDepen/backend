package com.github.sysdepen.depen_api.services;

import com.github.sysdepen.depen_api.entity.Admin;
import com.github.sysdepen.depen_api.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }

    public Admin update(Admin admin) {
        return adminRepository.save(admin);
    }

    public boolean deleteById(Long id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
