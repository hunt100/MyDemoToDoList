package com.example.demo.repository;

import com.example.demo.entity.MyUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyUserRepository extends CrudRepository<MyUser, Long> {
    Optional<MyUser> findByUserLogin(String userLogin);
}
