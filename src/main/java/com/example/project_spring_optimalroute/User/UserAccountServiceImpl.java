package com.example.project_spring_optimalroute.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    UserAcoountRepository userAcoountRepository;

    @Autowired
    public UserAccountServiceImpl(UserAcoountRepository userAcoountRepository) {
        this.userAcoountRepository = userAcoountRepository;
    }

    @Override
    public UserDTO login(String id, String pwd) {
        UserEntity userEntity = userAcoountRepository.findByUseridAndUserpwd(id, pwd);
        UserDTO userDTO = UserDTO.builder()
                .userid(userEntity.getUserid())
                .useremail(userEntity.getUseremail())
                .regidate(userEntity.getRegidate())
                .build();
        return userDTO;
    }

    @Override
    public boolean register(UserDTO userDTO) {

        UserEntity userEntity = UserEntity.builder()
                .userid(userDTO.getUserid())
                .userpwd(userDTO.getUserpwd())
                .useremail(userDTO.getUseremail())
                .build();

        userAcoountRepository.save(userEntity);

        return true;
    }

    @Override
    public boolean unregister(String userid) {
        userAcoountRepository.delete(userAcoountRepository.findByUserid(userid));
        return true;
    }
}
