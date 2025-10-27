package com.github.sysdepen.depen_api.controller;

import java.util.List;
import java.util.Optional;

import com.github.sysdepen.depen_api.entity.Protocols;
import com.github.sysdepen.depen_api.services.ProtocoloService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/protocols")
public class ProtocolController {
    @Autowired
    private ProtocoloService protocoloService;

    @GetMapping
    public ResponseEntity<List<Protocols>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(protocoloService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Protocols> findById(@PathVariable Long id) {
        return protocoloService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @PostMapping("/save")
    public ResponseEntity<Protocols> create(@RequestBody @Valid Protocols protocols) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(protocoloService.save(protocols));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Protocols> update(@RequestBody Protocols protocols) {
        return ResponseEntity.status(HttpStatus.OK).body(protocoloService.update(protocols));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = protocoloService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
