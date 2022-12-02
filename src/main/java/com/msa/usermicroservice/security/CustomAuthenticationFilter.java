package com.msa.usermicroservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msa.usermicroservice.domain.User;
import com.msa.usermicroservice.dto.ResponseComDto;
import com.msa.usermicroservice.dto.UserDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final Environment environment;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, Environment environment) {
        super(authenticationManager);
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            UserDto.signIn dto = objectMapper.readValue(request.getInputStream(), UserDto.signIn.class);

            String username = dto.getUserId();
            String password = dto.getPassword();

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetail customUserDetail = ((CustomUserDetail) authResult.getPrincipal());
        User user = customUserDetail.getUser();
        LocalDateTime afterOneDay = LocalDateTime.now().plusDays(1);
        Instant instant = afterOneDay.toInstant(ZoneOffset.UTC);
        Date date = Date.from(instant);

        UserDto.show obj = UserDto.show.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .username(user.getUsername())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String subject = objectMapper.writeValueAsString(obj);
        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(subject)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, environment.getProperty("jwt.secret"))
                .compact();

        String json = objectMapper.writeValueAsString(
                ResponseComDto.builder()
                        .resultMsg("successfully login.")
                        .resultObj(obj)
                        .build());

        byte[] bytes = subject.getBytes();
        String compareTarget = Base64.getEncoder().encodeToString(bytes);

        PrintWriter out = response.getWriter();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Authorization", token);
        response.addHeader("AuthContent", compareTarget);

        out.print(json);
        out.flush();
    }
}
