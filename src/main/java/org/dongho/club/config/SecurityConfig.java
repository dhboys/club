package org.dongho.club.config;

import lombok.extern.log4j.Log4j2;
import org.dongho.club.security.filter.ApiCheckFilter;
import org.dongho.club.security.filter.ApiLoginFilter;
import org.dongho.club.security.handler.ApiLoginFailHandler;
import org.dongho.club.security.handler.ClubLoginSuccessHandler;
import org.dongho.club.security.util.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true , securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ClubLoginSuccessHandler successHandler(){
        return new ClubLoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public ApiCheckFilter apiCheckFilter(){
        return new ApiCheckFilter("/notes/**/*" , jwtUtil());
    }

    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception{
        ApiLoginFilter apiLoginFilter =
                new ApiLoginFilter("/api/login" , jwtUtil());
        // 인증 매니저 지정
        apiLoginFilter.setAuthenticationManager(authenticationManager());
        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());

        return apiLoginFilter;
    }

    @Bean
    public JWTUtil jwtUtil(){
        return new JWTUtil();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/sample/all").permitAll()
//                .antMatchers("/sample/member").hasRole("USER");

        // member로 들어가면 자동으로 login 페이지로 넘어가게 한다
        http.formLogin();
        // csrf 비활성화
        http.csrf().disable();
        http.logout();

        http.oauth2Login().successHandler(successHandler());
        // 7일 저장
        http.rememberMe().tokenValiditySeconds(60*60*7).userDetailsService(userDetailsService());
        // apiCheckFilter의 순서를 Username... filter 전으로 옮기는 코드
        http.addFilterBefore(apiCheckFilter() , UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(apiLoginFilter() , UsernamePasswordAuthenticationFilter.class);

        // 모든 리소스가 다 로그인이 걸린다
        //super.configure(http);
    }

}
