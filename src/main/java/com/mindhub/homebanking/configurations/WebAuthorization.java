package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class WebAuthorization {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                // ALL
                .antMatchers("/web/index.html",
                        "/web/css/style.css",
                        "/web/js/index.js",
                        "/web/img/**").permitAll()

                // ALL (POST)
                .antMatchers(HttpMethod.POST,
                        "/api/login",
                        "/api/logout",
                        "/api/clients").permitAll()

                // CLIENT AND ADMIN
                .antMatchers("/logout.html",
                        "/web/accounts.html",
                        "/web/css/**",
                        "/web/js/**").hasAnyAuthority("CLIENT", "ADMIN")

                // CLIENT (POST)
                .antMatchers(HttpMethod.POST,
                        //"/api/clients/**",
                        "/api/clients/current/accounts",
                        "/api/clients/current/cards",
                        "/api/transactions",
                        "/api/loans").hasAuthority("CLIENT")

                // CLIENT
                .antMatchers("/web/account.html**",
                        "/web/cards.html",
                        "/web/transfers.html",
                        "/web/loan-application.html",
                        "/web/create-cards.html",
                        "/web/delete-cards.html",
                        "/api/clients/current",
                        "/api/accounts/**",
                        "/api/clients/current/accounts",
                        "/api/loans").hasAuthority("CLIENT")

                // ADMIN
                .antMatchers("/admin/**",
                        "/manager.html",
                        "/rest/**",
                        "/h2-console/**",
                        "/h2-console").hasAuthority("ADMIN")

                // Deny permission to all requests that have not been explicitly granted.
                .anyRequest().denyAll();



        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");



        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");


        // Turn off checking for CSRF tokens

        http.csrf().disable();



        // Disable frameOptions so h2-console can be accessed

        http.headers().frameOptions().disable();
        //httpSecurity.headers().frameOptions().disable();
        //http.headers().disable();

        // If user is not authenticated, just send an authentication failure response

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // If login is successful, just clear the flags asking for authentication

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // If login fails, just send an authentication failure response

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // If logout is successful, just send a success response

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }
    }
}
