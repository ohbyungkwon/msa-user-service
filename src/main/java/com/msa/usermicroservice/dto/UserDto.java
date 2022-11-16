package com.msa.usermicroservice.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class UserDto {
    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class signIn{
        private String userId;
        private String password;
    }

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class show implements Serializable {
        private String userId;
        private String username;
        private String email;
        private String mobile;
    }

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class signUp {
        @NotBlank(message = "사용자 아이디 길이를 확인해주세요.")
        @Size(min = 6, max = 20)
        private String userId;

        @NotBlank(message = "사용자명 길이를 확인해주세요.")
        @Size(min = 6, max = 20)
        private String username;

        @Email
        @NotBlank(message = "이메일을 확인해주세요.")
        @Size(min = 6, max = 100)
        private String email;

        @NotBlank(message = "휴대폰번호를 확인해주세요.")
        @Size(min = 11, max = 11)
        private String mobile;

        @NotBlank(message = "비밀번호를 확인해주세요.")
        @Size(min = 8, max = 30)
        private String password;
    }
}
