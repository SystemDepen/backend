package com.github.sysdepen.depen_api.services;

import java.util.List;
import java.util.Optional;

import com.github.sysdepen.depen_api.entity.Address;
import com.github.sysdepen.depen_api.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;


    public Address save(Address address) {
        System.out.println(address);
        return addressRepository.save(address);
    }


    public List<Address> findAll() {
        return addressRepository.findAll();
    }


    public Optional<Address> findById(Long id) {
        return addressRepository.findById(id);
    }


    public Address update(Address address) {
        return addressRepository.save(address);
    }


    public void deleteById(Long id) {
    	addressRepository.deleteById(id);
    }
}