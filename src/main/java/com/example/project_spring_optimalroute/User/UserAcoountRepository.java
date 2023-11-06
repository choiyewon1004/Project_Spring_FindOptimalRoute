package com.example.project_spring_optimalroute.User;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAcoountRepository extends JpaRepository<UserEntity, Integer>{

    UserEntity findByUserid(String userid);
    UserEntity findByUseridAndUserpwd(String userid, String userpwd);
}
