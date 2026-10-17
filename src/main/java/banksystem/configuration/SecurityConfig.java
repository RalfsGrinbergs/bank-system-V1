package banksystem.configuration;

import banksystem.exceptions.GlobalAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final GlobalAccessDeniedHandler globalAccessDeniedHandler;

    public SecurityConfig(GlobalAccessDeniedHandler globalAccessDeniedHandler) {
        this.globalAccessDeniedHandler = globalAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity
                .httpBasic(Customizer.withDefaults())
                .csrf(c -> c.disable())
                .exceptionHandling(e -> e.accessDeniedHandler(globalAccessDeniedHandler))
                .authorizeHttpRequests(auth -> auth.requestMatchers(
                        "/", "/login", "/users/registration", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/users/admin", "/accounts/admin", "/transactions/admin")
                        .hasRole("ADMIN")



                        .anyRequest().authenticated()

                ).build();




    }

}
