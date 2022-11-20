package com.brahim.repository;

import com.brahim.model.User;
import com.brahim.projection.Test;
import com.brahim.projection.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
     //User findByUsername(String username);
     User findByEmail(String email);

     // search all users
     @Query("select new com.brahim.projection.UserProjection(u.id,u.name,u.contact,u.email, r.name  as role,u.status) from User u  join u.roles r   where r.name='ROLE_USER'")
     List<UserProjection> getAllUsers();

     // search all admins
     @Query("select new com.brahim.projection.UserProjection(u.id,u.name,u.contact,u.email, r.name  as role,u.status) from User u  join u.roles r   where r.name='ROLE_ADMIN'")
     List<UserProjection> getAllAdmins();


}
