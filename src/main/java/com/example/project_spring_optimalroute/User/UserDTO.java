package com.example.project_spring_optimalroute.User;

import lombok.Builder;
import lombok.Getter;
import java.util.Date;
@Getter
@Builder
public class UserDTO {
    private String userid;
    private String userpwd;
    private String useremail;
    private Date regidate;

}
