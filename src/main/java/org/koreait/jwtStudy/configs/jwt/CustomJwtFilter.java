package org.koreait.jwtStudy.configs.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class CustomJwtFilter extends GenericFilterBean { //로그인 상태를 유지하기 위한 필터

    private final TokenProvider tokenProvider;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;

        //요청 헤더 Authorization 항목의 JWT 토큰 추출 S
        String header = req.getHeader("Authorization");
        String jwt = null;
        if(StringUtils.hasText(header)){ //NPE방지를 위해 스프링 편의 기능인 StringUtils로 (header != null && !header.isBlank())로 작성하는 것을 대체할 수 있다. 즉 null 또는 비어있는지 체크해준다
            //Bearer ... <7번째자리부터 토큰이다
            jwt = header.substring(7);
        }
        //요청 헤더 Authorization 항목의 JWT 토큰 추출 E

        //로그인 유지 처리 S
        if(StringUtils.hasText(jwt)){
            tokenProvider.validateToken(jwt); // 토큰 이상 시 예외 발생함(변조 또는 만료)
            // 토큰이 넘어 오면 로그인 유지되도록
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //로그인 유지 처리 E



        chain.doFilter(request,response);



    }
}
