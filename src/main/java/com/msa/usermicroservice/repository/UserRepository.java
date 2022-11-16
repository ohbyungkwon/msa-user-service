package com.msa.usermicroservice.repository;

import com.msa.usermicroservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Boolean existsUserByUserId(String userId);
}
