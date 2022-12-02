package com.msa.usermicroservice.config;

import com.msa.usermicroservice.security.CustomAuthenticationFilter;
import com.msa.usermicroservice.security.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final static String loginUrl = "/login";

    @Autowired
    private CustomAuthenticationProvider authenticationProvider;
    @Autowired
    private Environment environment;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CustomAuthenticationFilter authenticationFilter() throws Exception{
        CustomAuthenticationFilter filter =  new CustomAuthenticationFilter(
                authenticationManagerBean(), environment);
        filter.setFilterProcessesUrl(loginUrl);
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests().antMatchers("/actuator/**").permitAll()
            .and()
            .authorizeRequests().antMatchers(HttpMethod.POST, "/user").permitAll()
            .and()
            .authorizeRequests().antMatchers("/user/**")
            .authenticated()
            .and()
            .addFilter(authenticationFilter());
    }
}
