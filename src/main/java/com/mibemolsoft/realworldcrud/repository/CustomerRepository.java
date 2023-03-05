package com.mibemolsoft.realworldcrud.repository;

import com.mibemolsoft.realworldcrud.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// The best way to write a custom Spring Data Repository https://vladmihalcea.com/custom-spring-data-repository/
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
