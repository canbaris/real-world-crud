package com.mibemolsoft.realworldcrud.repository;

import com.mibemolsoft.realworldcrud.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,String> {

}
