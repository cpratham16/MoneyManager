package com.project.moneymanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.moneymanager.entity.ProfileEntity;

public interface ProfileRepository extends JpaRepository<ProfileEntity,Long >{

    //select emailBy from tbl_profile
    Optional<ProfileEntity>findByEmail(String email);
    
}
