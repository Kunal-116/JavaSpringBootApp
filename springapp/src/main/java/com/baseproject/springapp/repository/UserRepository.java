
/*  It is use to perform CRUD and finding user by name */

package com.baseproject.springapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baseproject.springapp.model.AppUsers;


public interface UserRepository extends JpaRepository<AppUsers,Long>{
    // Optional<AppUsers> findByUName(String u_name);       // if you want search by username
    // Optional<AppUsers> findByUEmail(String u_email);     // if you want search by email
   Optional<AppUsers> findByumobile(String UMobile);
boolean existsByumobile(String UMobile);
    //boolean existsByEmail(String email);
}
