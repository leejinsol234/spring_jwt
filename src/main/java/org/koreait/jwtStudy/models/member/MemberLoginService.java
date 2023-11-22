package org.koreait.jwtStudy.models.member;

import lombok.RequiredArgsConstructor;
import org.koreait.jwtStudy.api.controllers.members.RequestLogin;
import org.koreait.jwtStudy.configs.jwt.TokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberLoginService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public String login(RequestLogin form){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(form.email(),form.password()); //검증(email,pw 체크)
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken); //검증 시 일치하면 authentication 반환

        String accessToken = tokenProvider.createToken(authentication); //JWT 토큰 발급

        return accessToken;
    }
}
