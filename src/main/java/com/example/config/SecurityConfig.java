package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    /*
      @Bean
      public UserDetailsService userDetailsService() {
          return new MyUserDetailsService();
      }

      @Bean
          public AuthenticationProvider  authenticationProvider() {
          DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
          provider.setUserDetailsService(userDetailsService());
          provider.setPasswordEncoder(passwordEncoder());
          return provider;
      }

          @Bean
          public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
              http
                      .csrf(csrf -> csrf.disable())
                      .authorizeHttpRequests(auth -> auth
                              .requestMatchers("/index").hasAnyRole( "USER")
                                      .requestMatchers("/ws/**").permitAll()
                                      .requestMatchers("/registrar/**").permitAll()
                                      .requestMatchers("/printer").permitAll()
                                      .requestMatchers("/register").permitAll()
                                      .requestMatchers("/admin/register").permitAll()
                                      .requestMatchers("/admin/tickets").hasAnyRole("ADMIN")
                                      .requestMatchers("/report").hasAnyRole("ADMIN")
                                      .requestMatchers("/notifications/**").hasAnyRole("ADMIN")
                                      .requestMatchers("/rup/**").hasAnyRole("ADMIN")
                                      .requestMatchers("/booking/**").hasAnyRole("ADMIN")
       //                       .requestMatchers("/resources/**").hasAnyRole("ADMIN", "USER")
                              .anyRequest().hasAnyRole("ADMIN", "USER") // Остальные запросы требуют одной из ролей

                      )
                      .formLogin(form -> form
                             .loginPage("/login").
                              permitAll() // Позволяем всем доступ к странице логина
                              .successHandler(new CustomAuthenticationSuccessHandler())
                              // .defaultSuccessUrl("/tickets/new", true) // true указывает на перенаправление даже если был предыдущий URL
                              .failureUrl("/login?error=true")
                      )
                      .logout(logout -> logout
                              .permitAll() // Позволяем всем доступ к логауту
                      );

              return http.build();
          }
    */
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

       @Autowired
       @Lazy
        private OracleAuthenticationProvider oracleAuthProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().permitAll()
                    )
                    .formLogin(form -> form
                            .loginPage("/login")
                            .permitAll()
                            .successHandler((request, response, authentication) -> {
                                response.sendRedirect("/registrar");
                            })
                    );

            return http.build();
        }

        @Bean
       public AuthenticationManager authManager(HttpSecurity http) throws Exception {
           return http.getSharedObject(AuthenticationManagerBuilder.class)
                    .authenticationProvider(oracleAuthProvider)
                    .build();
       }



    }

