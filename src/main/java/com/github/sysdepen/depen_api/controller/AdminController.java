package com.github.sysdepen.depen_api.controller;

import com.github.sysdepen.depen_api.entity.Admin;
import com.github.sysdepen.depen_api.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/admins")
@CrossOrigin("*")
@Validated
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public ResponseEntity<List<Admin>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Admin>> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Admin> create(@Valid @RequestBody Admin admin) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.save(admin));
    }

    @PutMapping
    public ResponseEntity<Admin> update(@Valid @RequestBody Admin admin) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.update(admin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
