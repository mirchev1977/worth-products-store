package com.w.prod.config;


import com.w.prod.services.impl.WorthProductUserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final WorthProductUserService worthProductUserService;
    private final PasswordEncoder passwordEncoder;

    public ApplicationSecurityConfig(WorthProductUserService worthProductUserService, PasswordEncoder passwordEncoder) {
        this.worthProductUserService = worthProductUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/", "/users/login", "/users/register",
                        "/blueprints/all", "/results/*").permitAll()
                .antMatchers("/products/archive/*", "/products/delete/*", "/products/update/*",
                        "/blueprints/accept/*", "/blueprints/delete/*", "/users/manage",
                        "/users/delete/*", "/activities/add", "/roles/add", "/statistics").hasRole("ADMIN")
                .antMatchers("/blueprints/add", "/blueprints/details/*",
                        "/products/owned", "/products/all",
                        "/products/details/*", "/products/update/*", "/products/join/*", "/products/leave/*", "/products/publish/*",
                        "/products/own", "/products/api", "/products/collaborations",
                        "/blueprints/accept/*", "/blueprints/delete/*", "/users/manage", "/users/delete/*", "/activities/add").hasRole("USER")
                .and()
                .formLogin()
                .loginPage("/users/login")
                .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                .defaultSuccessUrl("/home")
                .failureForwardUrl("/users/login-error")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(worthProductUserService)
                .passwordEncoder(passwordEncoder);
    }
}
