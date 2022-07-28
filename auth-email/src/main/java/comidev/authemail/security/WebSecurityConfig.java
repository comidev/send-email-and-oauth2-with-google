package comidev.authemail.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {
    private final OAuth2Filter oAuth2Filter;
    // private final OAuth2UserService oAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests((authorize) -> authorize
                        .antMatchers("/auths/login/oauth2/google").authenticated()
                        .anyRequest().permitAll())
                .oauth2Login()
                // .userInfoEndpoint()
                // .userService(oAuth2UserService)
                // .and()
                .successHandler(oAuth2Filter)
                .and()
                .build();
    }
}
