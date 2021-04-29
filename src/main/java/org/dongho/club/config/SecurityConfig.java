package org.dongho.club.config;

import lombok.extern.log4j.Log4j2;
import org.dongho.club.security.handler.ClubLoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ClubLoginSuccessHandler successHandler(){
        return new ClubLoginSuccessHandler(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/sample/all").permitAll()
                .antMatchers("/sample/member").hasRole("USER");

        // member로 들어가면 자동으로 login 페이지로 넘어가게 한다
        http.formLogin();
        // csrf 비활성화
        http.csrf().disable();
        http.logout();

        http.oauth2Login().successHandler(successHandler());
        // 7일 저장
        http.rememberMe().tokenValiditySeconds(60*60*7).userDetailsService(userDetailsService());

        // 모든 리소스가 다 로그인이 걸린다
        //super.configure(http);
    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user1")
//                .password("$2a$10$w//BSOK7W1kMb8TZb/lhXOkkSUvTZxHBZzGfvrHxMV4TT4pmUe1zy")
//                .roles("USER");
//        // roles는 ROLE_ 생략가능
//    }
}
