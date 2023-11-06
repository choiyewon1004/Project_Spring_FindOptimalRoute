package com.example.project_spring_optimalroute.Controller;


import com.example.project_spring_optimalroute.User.UserAccountService;
import com.example.project_spring_optimalroute.User.UserDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "유저 API", description = "USER API")
@RequiredArgsConstructor
@RestController
@Slf4j
@CrossOrigin
public class userController {

    private final UserAccountService userAccountService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestBody Map<String, String> requestBody) {
        String id = requestBody.get("userName");
        String pwd = requestBody.get("password");

        log.info("로그인 요청 : ID: {} PASS: {}", id, pwd);

        UserDTO userDTO = userAccountService.login(id, pwd);

        if (userDTO == null) {
            log.warn("로그인 실패");
        }

        log.info("로그인 성공");
        return ResponseEntity.ok().body(userDTO);
    }



    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestParam("userid") String id, @RequestParam("userpwd") String pwd, @RequestParam("useremail") String email) {

        log.info("유저 회원가입 요청 : ID : {} PASS : {} NAME : {}" ,id,pwd,email);

        UserDTO userDTO = UserDTO.builder()
                .userid(id)
                .userpwd(pwd)
                .useremail(email)
                .build();

        userAccountService.register(userDTO);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    // 회원탈퇴
    @DeleteMapping("/unregister")
    public ResponseEntity unRegisterUser(@RequestParam String userid) {
        log.info("회원 탈퇴 요청 : userid : {}", userid);

        userAccountService.unregister(userid);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
