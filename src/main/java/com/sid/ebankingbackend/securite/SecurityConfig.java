package com.sid.ebankingbackend.securite;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.sid.ebankingbackend.securite.service.UserDetailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)

public class SecurityConfig {
    @Value("${jwt.secret}")
private String  secretKey;

    private UserDetailServiceImpl userDetailService;

  //  private PasswordEncoder passwordEncoder;

    public SecurityConfig(@Lazy UserDetailServiceImpl userDetailsService) {
        this.userDetailService = userDetailsService;
    }
   // @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(passwordEncoder().encode("1234")).authorities("USER").build(),
                User.withUsername("admin").password(passwordEncoder().encode("1234")).authorities("USER","ADMIN").build()
                );
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(ar->ar.requestMatchers("/auth/login/**","/swagger-ui/**","/v3/api-docs/**").permitAll())
                .authorizeHttpRequests(ar->ar.anyRequest().authenticated())
                .cors(Customizer.withDefaults())
              //  .httpBasic(Customizer.withDefaults() )
                .oauth2ResourceServer(oa->oa.jwt(Customizer.withDefaults()))
             .userDetailsService(userDetailService)
                .build();

    }
    @Bean
    JwtEncoder jwtEncoder(){
//        String secretKey="9faa21g25gf363ff54ff55gfd565655fg454gf54gf5fgh45hg545h4g54f5d554";
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes()));
    }
    @Bean
    JwtDecoder jwtDecoder(){
//        String secretKey="9faa21g25gf363ff54ff55gfd565655fg454gf54gf5fgh45hg545h4g54f5d554";

        SecretKeySpec secretKeySpec=new SecretKeySpec(secretKey.getBytes(), "RSA");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
    }
@Bean
     AuthenticationManager authenticationManager(UserDetailsService userDetailsService){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
       //   corsConfiguration.setExposedHeaders(List.of("x-auth-token"));
        UrlBasedCorsConfigurationSource source =new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;

    }
}
