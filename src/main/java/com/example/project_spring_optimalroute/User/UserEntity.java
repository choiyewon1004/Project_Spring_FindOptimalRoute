package com.example.project_spring_optimalroute.User;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(name = "id")
    private String userid;

    @Column(name = "pwd")
    private String userpwd;

    @Column(name = "email")
    private String useremail;

    @Column(name = "reg_date")
    private Date regidate;

    @Builder
    public UserEntity(String userid, String userpwd, String useremail){
        this.userid = userid;
        this.userpwd = userpwd;
        this.useremail = useremail;
        this.regidate = new Date();
    }
}