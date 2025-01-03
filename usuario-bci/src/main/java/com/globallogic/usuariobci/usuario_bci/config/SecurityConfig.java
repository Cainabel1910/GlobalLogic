package com.globallogic.usuariobci.usuario_bci.config;


import com.globallogic.usuariobci.usuario_bci.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        RequestMatcher h2ConsoleMatcher = new AntPathRequestMatcher("/h2-console/**");
        RequestMatcher authMatcher = new AntPathRequestMatcher("/auth/**");
        RequestMatcher swaggerMatcher = new AntPathRequestMatcher("/**");
        RequestMatcher docsMatcher = new AntPathRequestMatcher("/v2/api-docs/**");
        RequestMatcher sresourcesMatcher = new AntPathRequestMatcher("**/swagger-resources/**");


        return http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(authRequest  -> {
                                authRequest
                                        .requestMatchers(h2ConsoleMatcher, authMatcher, swaggerMatcher, docsMatcher,sresourcesMatcher).permitAll()
                                        .anyRequest().authenticated();
                            }
                         )
                    //.formLogin(withDefaults())
                    .sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authProvider)
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
    }
}


