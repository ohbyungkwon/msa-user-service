package com.msa.usermicroservice.domain;

import com.msa.usermicroservice.dto.UserDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
public class User {
    @Id
    private String userId;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 11)
    private String mobile;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    public UserDto.show convertUserShow(User user){
        return UserDto.show.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .build();
    }

    public static User createUser(UserDto.signUp signUp){
        return User.builder()
                .userId(signUp.getUserId())
                .username(signUp.getUsername())
                .email(signUp.getEmail())
                .mobile(signUp.getMobile())
                .password(signUp.getPassword())
                .build();
    }
}
