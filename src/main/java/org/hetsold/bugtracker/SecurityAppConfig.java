package org.hetsold.bugtracker;

import org.hetsold.bugtracker.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@ComponentScan(
        basePackages = {"org.hetsold.bugtracker.service"})
@EnableGlobalAuthentication
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true)
public class SecurityAppConfig extends WebSecurityConfigurerAdapter {

    private SecurityService securityService;

    @Autowired
    public SecurityAppConfig(SecurityService securityService) {
        this.securityService = securityService;
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .invalidSessionUrl("/facelets/pages/login.jsf")
                .sessionAuthenticationErrorUrl("/facelets/pages/login.jsf");
        http
                .authorizeRequests()
                .antMatchers("/facelets/pages/login.jsf").permitAll()
                .antMatchers("/javax.faces.resource/**").permitAll()
                .antMatchers("/facelets/pages/userProfile.jsf").authenticated()
                .antMatchers("/facelets/pages/issues.jsf").hasAnyRole("LIST_ISSUES", "DELETE_ISSUE", "EDIT_ISSUE")
                .antMatchers("/facelets/pages/issueDetail.jsf").hasAnyRole("LIST_ISSUES", "DELETE_ISSUE", "EDIT_ISSUE")
                .antMatchers("/facelets/pages/tickets.jsf").hasAnyRole("LIST_TICKETS", "DELETE_TICKET", "EDIT_TICKET")
                .antMatchers("/facelets/pages/ticketDetail.jsf").hasAnyRole("LIST_TICKETS", "DELETE_TICKET", "EDIT_TICKET")
                .antMatchers("/facelets/pages/users.jsf").hasAnyRole("LIST_USERS", "DELETE_USER", "EDIT_USER")
                .anyRequest()
                .authenticated();
        http
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/facelets/pages/login.jsf");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public AuthenticationManager getManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(getPasswordEncoder());
        provider.setUserDetailsService(securityService);
        return provider;
    }
}
