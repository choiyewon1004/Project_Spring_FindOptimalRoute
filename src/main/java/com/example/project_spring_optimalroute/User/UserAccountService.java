package com.example.project_spring_optimalroute.User;

public interface UserAccountService {

    UserDTO login(String id, String pwd);

    boolean register(UserDTO userDTO);

    boolean unregister(String userid);
}
