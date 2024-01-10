package com.shopxy.ecom.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.shopxy.ecom.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  public AuthenticationSuccessHandler customSuccessHandler;

  @Bean
  public UserService getuDtlsService() {
    return new UserService();
  }

  @Bean
  public BCryptPasswordEncoder getBCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider getdaoAuthprovider(){
    DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(getuDtlsService());
    daoAuthenticationProvider.setPasswordEncoder(getBCryptPasswordEncoder());
    return daoAuthenticationProvider;
  }

  @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .authorizeHttpRequests(authz->authz

                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/**").permitAll()

                ).formLogin(login->
                  login.loginPage("/signin")
                  .loginProcessingUrl("/signin")
                  .permitAll()
                  .successHandler(customSuccessHandler)
                )
                .logout(logout -> logout
                .permitAll()
                        
                        .deleteCookies("JSESSIONID"))
                        
                .csrf(csrf->csrf.disable()
                );

        http
        .authenticationProvider(getdaoAuthprovider());

        return http.build();
    }
}
