package com.msa.usermicroservice.controller;

import com.msa.usermicroservice.dto.ResponseComDto;
import com.msa.usermicroservice.dto.UserDto;
import com.msa.usermicroservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseComDto> selectUser(@PathVariable String userId){
        UserDto.show user = userService.selectUser(userId);
        return new ResponseEntity<>(
                ResponseComDto.builder()
                        .resultMsg("사용자 조회 완료")
                        .resultObj(user)
                        .build(), HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<ResponseComDto> registerUser(@Valid @RequestBody UserDto.signUp signUp){
        UserDto.show user = userService.registerUser(signUp);
        return new ResponseEntity<>(
                ResponseComDto.builder()
                        .resultMsg("회원가입 완료")
                        .resultObj(user)
                        .build(), HttpStatus.OK);
    }
}