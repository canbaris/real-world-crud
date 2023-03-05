package com.mibemolsoft.realworldcrud.controllers;

import com.mibemolsoft.realworldcrud.domain.Customer;
import com.mibemolsoft.realworldcrud.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        try {
            List<Customer> customers = new ArrayList<>();
            customerRepository.findAll().forEach(customers::add);
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}