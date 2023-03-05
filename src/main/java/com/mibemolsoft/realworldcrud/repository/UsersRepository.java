package com.mibemolsoft.realworldcrud.repository;

import com.mibemolsoft.realworldcrud.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

}
