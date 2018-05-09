package com.wangyang.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private LogoutSuccessHandler myLogoutSucessHandler;

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .passwordEncoder(new MyPasswordEncoder())
                .dataSource(dataSource)
                .usersByUsernameQuery("select username,password,enabled from user_inf where username=?")
                .authoritiesByUsernameQuery("select username,authority from user_inf where username=?")
                .rolePrefix("ROLE_");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/*").permitAll()
            .and()
            .formLogin()
            .successHandler(new MyAuthenticationSuccessHandler())
            .and()
            .csrf()
                .disable()
            .logout()
                .logoutSuccessHandler(myLogoutSucessHandler);

    }
}
