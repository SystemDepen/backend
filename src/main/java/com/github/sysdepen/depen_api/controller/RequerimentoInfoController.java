package com.github.sysdepen.depen_api.controller;

import com.github.sysdepen.depen_api.entity.Protocols;
import com.github.sysdepen.depen_api.entity.RequerimentoInfo;
import com.github.sysdepen.depen_api.services.RequerimentosInfoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/req_camp")
public class RequerimentoInfoController {

    @Autowired
    private RequerimentosInfoService requerimentoInfoService;

    @GetMapping
    public ResponseEntity<List<RequerimentoInfo>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(requerimentoInfoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequerimentoInfo> findById(@PathVariable Long id) {
        return requerimentoInfoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/save")
    public ResponseEntity<RequerimentoInfo> create(@RequestBody @Valid RequerimentoInfo requerimentoInfo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(requerimentoInfoService.save(requerimentoInfo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequerimentoInfo> update(@RequestBody @Valid RequerimentoInfo requerimentoInfo) {
        return ResponseEntity.status(HttpStatus.OK).body(requerimentoInfoService.update(requerimentoInfo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = requerimentoInfoService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
