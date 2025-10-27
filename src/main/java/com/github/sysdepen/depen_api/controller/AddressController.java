package com.github.sysdepen.depen_api.controller;

import com.github.sysdepen.depen_api.entity.Address;
import com.github.sysdepen.depen_api.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/address")
@Validated
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public ResponseEntity<List<Address>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(addressService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> findById(@PathVariable Long id) {
        return addressService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/save")
    public ResponseEntity<Address> create(@Valid @RequestBody Address address) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.save(address));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Address> update(@RequestBody Address address) {
        return ResponseEntity.status(HttpStatus.OK).body(addressService.update(address));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = addressService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
