//package com.mqv.springgraphql.config;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.apache.logging.log4j.util.Strings;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Set;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfiguration {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf().disable()
//                .sessionManagement(spec -> spec.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .httpBasic().disable()
//                .formLogin().disable()
//                .authorizeHttpRequests(spec -> spec.requestMatchers("/graphql/**", "/graphiql/**").permitAll())
//                .addFilterBefore(new JwtFilterChain(), UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }
//}
//
//class JwtFilterChain extends OncePerRequestFilter {
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String bearerToken = request.getHeader("Authorization");
//        if (Strings.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            String token = bearerToken.substring("Bearer ".length());
//            AuthenticatedUser authenticatedUser = null;
//
//            if (token.equals("admin")) {
//                authenticatedUser = new AuthenticatedUser("ADMIN");
//            } else if (token.equals("user")) {
//                authenticatedUser = new AuthenticatedUser("USER");
//            }
//
//            if (authenticatedUser != null) {
//                SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}
//
//record AuthenticatedUser(String role) implements Authentication {
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Set.of(new SimpleGrantedAuthority("ROLE_" + role));
//    }
//
//    @Override
//    public Object getCredentials() {
//        return null;
//    }
//
//    @Override
//    public Object getDetails() {
//        return null;
//    }
//
//    @Override
//    public Object getPrincipal() {
//        return this;
//    }
//
//    @Override
//    public boolean isAuthenticated() {
//        return true;
//    }
//
//    @Override
//    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
//
//    }
//
//    @Override
//    public String getName() {
//        return "Authenticated User";
//    }
//}