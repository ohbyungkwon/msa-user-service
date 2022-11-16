package com.msa.usermicroservice.service;

import com.msa.usermicroservice.domain.User;
import com.msa.usermicroservice.domain.UserDetail;
import com.msa.usermicroservice.dto.UserDto;
import com.msa.usermicroservice.exception.BadReqException;
import com.msa.usermicroservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto.show selectUser(String userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadReqException("사용자가 존재하지 않습니다."));
        return user.convertUserShow(user);
    }

    @Transactional
    public UserDto.show registerUser(UserDto.signUp signUp) {
        String userId = signUp.getUserId();
        boolean isExist = userRepository.existsUserByUserId(userId);
        if(isExist){
            throw new BadReqException("이미 존재하는 계정입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signUp.getPassword());
        signUp.setPassword(encodedPassword);

        User user = User.createUser(signUp);
        return userRepository.save(user).convertUserShow(user);
    }


    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));

        return new UserDetail(user);
    }
}
