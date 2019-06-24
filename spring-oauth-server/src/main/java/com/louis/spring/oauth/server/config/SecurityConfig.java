package com.louis.spring.oauth.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll().antMatchers("/login", "/oauth/**").permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()    // 自定义登录页面，这里配置了 loginPage, 就会通过 LoginController 的 login 接口加载登录页面
                .and().csrf().disable();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        // 配置用户名密码，这里采用内存方式，生产环境需要从数据库获取
//        auth.inMemoryAuthentication()
//                .withUser("admin")
//                .password(passwordEncoder().encode("123"))
//                .roles("USER");

        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }


    /**
     * 配置用户
     * 使用内存中的用户，实际项目中，一般使用的是数据库保存用户，具体的实现类可以使用JdbcDaoImpl或者JdbcUserDetailsManager
     * @return
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin").password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123")).authorities("USER").build());
        return manager;
    }
    @Override
    @Bean //分享到oauth2
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
