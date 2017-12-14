package com.fantosoft.arest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fantosoft.arest.entity.DemoUser;

@Repository
public interface UserJpaRepository extends JpaRepository<DemoUser, Long> {
        DemoUser findByName(String name);
}