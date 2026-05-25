package com.example.portfolio_health_monitor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import de.codecentric.boot.admin.server.config.AdminServerProperties;

@Configuration
public class SecurityConfig {
    private final AdminServerProperties adminServerProperties;

    public SecurityConfig(
        AdminServerProperties adminServerProperties
    ) {
        this.adminServerProperties = adminServerProperties;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler =
            new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(this.adminServerProperties.getContextPath() + "/");

        httpSecurity.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(this.adminServerProperties.getContextPath() + "/assets/**").permitAll()
                .requestMatchers(this.adminServerProperties.getContextPath() + "/login").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage(adminServerProperties.getContextPath() + "/login").successHandler(successHandler)
            )
            .logout(logout -> logout
                .logoutUrl(adminServerProperties.getContextPath() + "/logout")
                .logoutSuccessUrl(adminServerProperties.getContextPath() + "/login?logout")
                .permitAll()
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers(
                    this.adminServerProperties.getContextPath() + "/instances",
                    this.adminServerProperties.getContextPath() + "/actuator/**",
                    this.adminServerProperties.getContextPath() + "/logout"
                )
            );

        return httpSecurity.build();
    }
}
