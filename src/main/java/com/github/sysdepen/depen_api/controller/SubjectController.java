package com.github.sysdepen.depen_api.controller;

import java.util.List;
import java.util.Optional;

import com.github.sysdepen.depen_api.entity.Subject;
import com.github.sysdepen.depen_api.services.SubjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/users/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public ResponseEntity<List<Subject>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Subject>> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Subject> create(@RequestBody @Valid Subject subject) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.save(subject));
    }

    @PutMapping
    public ResponseEntity<Subject> update(@RequestBody Subject subject) {
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.update(subject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        subjectService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}