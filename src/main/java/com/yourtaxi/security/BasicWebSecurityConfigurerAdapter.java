package com.yourtaxi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class BasicWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter
{

    @Value("${spring.security.authentication.basic.user}")
    private String user;

    @Value("${spring.security.authentication.basic.password}")
    private String password;

    @Value("${spring.security.authentication.basic.disableCSRF}")
    private boolean disableCSRF;


    /**
     * Create user from yml file for Authenticate
     *
     * @param auth {@link AuthenticationManagerBuilder}
     * @throws Exception case happen something wrong
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {

        auth.inMemoryAuthentication()
            .withUser(this.user)
            .password(this.password)
            .authorities("USER");
    }


    /**
     * Configure endpoints and permissions
     *
     * @param http {@link HttpSecurity}
     * @throws Exception case happen something wrong
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {

        http.authorizeRequests()
            .antMatchers("/**")
            .authenticated()
            .and()
            .httpBasic();

        if (!this.disableCSRF)
        {
            return;
        }

        http.csrf()
            .disable();
    }
}
