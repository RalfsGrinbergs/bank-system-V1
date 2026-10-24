package banksystem.configuration;

import banksystem.exceptions.GlobalAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final GlobalAccessDeniedHandler globalAccessDeniedHandler;
    private final JwtFilter jwtFilter;


    public SecurityConfig(GlobalAccessDeniedHandler globalAccessDeniedHandler, JwtFilter jwtFilter) {
        this.globalAccessDeniedHandler = globalAccessDeniedHandler;
        this.jwtFilter = jwtFilter;

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(c -> c.disable())
                .exceptionHandling(e -> e.accessDeniedHandler(globalAccessDeniedHandler))
                .authorizeHttpRequests(auth -> auth.requestMatchers(
                        "/", "/users/login", "/users/registration", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/users/admin", "/accounts/admin", "/transactions/admin")
                        .hasRole("ADMIN")



                        .anyRequest().authenticated()

                ).build();




    }
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

}
