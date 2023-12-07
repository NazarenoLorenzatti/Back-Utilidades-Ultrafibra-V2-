package net.ultrafibra.cotrasenas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import net.ultrafibra.cotrasenas.security.UserDetailsServiceImp;
import org.springframework.http.HttpMethod;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImp userDetailsService;

    public SecurityConfig() {
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsServiceImp userDetailService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeHttpRequests(authorizeRequests ->
//                authorizeRequests
//                    .requestMatchers(HttpMethod.OPTIONS).permitAll()
//                    .requestMatchers("/login","/primer-factor", "/api/v1/usuario/crear-usuario", "/prueba") // Permitir todas las rutas
//                    .permitAll()
//                    .anyRequest()
//                    .authenticated()
//    
//            )                
//            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
//            .sessionManagement(sessionManagement ->
//                sessionManagement
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            )
//            .csrf().disable()
//            .cors();
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors().and()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                .requestMatchers("/login","/primer-factor", "/api/v1/usuario/crear-usuario", "/prueba").permitAll()               
                .anyRequest()
                .authenticated()
                .and()
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .build();
    }
//    
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .cors().and()
//                .csrf().disable()
//                .authorizeRequests()
//                .requestMatchers("/prueba").permitAll() // Ruta pública sin autenticación
//                .anyRequest().authenticated()
//                .and()
//                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))                
//                .build();
//    }

}
