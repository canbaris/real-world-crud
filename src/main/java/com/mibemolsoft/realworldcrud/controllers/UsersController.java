package com.mibemolsoft.realworldcrud.controllers;

import com.mibemolsoft.realworldcrud.domain.Customer;
import com.mibemolsoft.realworldcrud.domain.Users;
import com.mibemolsoft.realworldcrud.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RestController
public class UsersController {

    @Autowired
    UsersRepository usersRepository;

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getAllUsers() {
        try {
            List<Users> users = new ArrayList<>();
            usersRepository.findAll().forEach(users::add);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}