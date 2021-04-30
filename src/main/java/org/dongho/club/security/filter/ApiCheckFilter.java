package org.dongho.club.security.filter;

import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.dongho.club.security.util.JWTUtil;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;
    private JWTUtil jwtUtil;

    public ApiCheckFilter(String pattern , JWTUtil jwtUtil){
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("REQUESTURI: " + request.getRequestURI());

        log.info(antPathMatcher.match(pattern, request.getRequestURI()));
        // pattern 이 일치하면 다음 호출 없이 return만 해준다
        if(antPathMatcher.match(pattern, request.getRequestURI())) {

            log.info("ApiCheckFilter.................................................");
            log.info("ApiCheckFilter.................................................");
            log.info("ApiCheckFilter.................................................");
            // header를 체크
            boolean checkHeader = checkAuthHeader(request);
            // header가 맞으면 다음 호출 진행시킨다
            if(checkHeader){
                filterChain.doFilter(request,response);
                return;
            }else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                // json 리턴
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "FAIL CHECK API TOKEN";
                json.put("code", "403");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
                return;
            }
        }
        // 다음 호출이 이어지게 하는 코드
        filterChain.doFilter(request, response);
    }
/*    // header를 체크하는 메서드
    private boolean checkHeader(HttpServletRequest request) {

        boolean result = false;
        // 헤더 추출
        String authHeader = request.getHeader("Authorization");
        // header 비교
        if(StringUtils.hasText(authHeader)){
            log.info("Authorization exist: " + authHeader);

            if(authHeader.equals("12345678")){
                result = true;
            }  // end if
        } // end if
        return result;
    }*/

    private boolean checkAuthHeader(HttpServletRequest request) {

        boolean checkResult = false;

        String authHeader = request.getHeader("Authorization");

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            log.info("Authorization exist: " + authHeader);

            try {
                String email = jwtUtil.validateAndExtract(authHeader.substring(7));
                log.info("validate result: " + email);
                checkResult =  email.length() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return checkResult;
    }
}
