package org.dongho.club.security.filter;

import lombok.extern.log4j.Log4j2;
import org.dongho.club.security.dto.ClubAuthMemberDTO;
import org.dongho.club.security.util.JWTUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    private JWTUtil jwtUtil;

    // 어떤 URL로 들어왔을 때만 처리할지
    public ApiLoginFilter(String defaultFilterProcessesUrl , JWTUtil jwtUtil) {
        super(defaultFilterProcessesUrl);
        this.jwtUtil = jwtUtil;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        log.info("ApiLoginFilter.....................................");
        String email = request.getParameter("email");
        String pw = request.getParameter("pw");

        if(email == null){
            throw new BadCredentialsException("Email Can not be null");
        }
        // 실제 인증 작업을 위한 토큰
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email , pw);
        // Manager 불러와서 인증
        return getAuthenticationManager().authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("-----------------ApiLoginFilter---------------------");
        log.info("successfulAuthentication: " + authResult);

        // login 처리된 정보(ClubAuthMemberDTO)반환
        log.info(authResult.getPrincipal());

        //email address
        String email = ((ClubAuthMemberDTO)authResult.getPrincipal()).getUsername();

        String token = null;
        try {
            token = jwtUtil.generateToken(email);

            response.setContentType("text/plain");
            response.getOutputStream().write(token.getBytes());

            log.info(token);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
