package org.koreait.jwtStudy.configs.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.koreait.jwtStudy.commons.Utils;
import org.koreait.jwtStudy.commons.exceptions.BadRequestException;
import org.koreait.jwtStudy.models.member.MemberInfo;
import org.koreait.jwtStudy.models.member.MemberInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TokenProvider {

    public final String secret;
    public final long tokenValidityInSeconds;
    @Autowired
    private MemberInfoService infoService;

    private Key key;

    public TokenProvider(String secret, Long tokenValidityInSeconds){ //HMAC -> SHA512 + Message
        this.secret = secret;
        this.tokenValidityInSeconds = tokenValidityInSeconds;
        byte[] bytes = Decoders.BASE64.decode(secret);
        key= Keys.hmacShaKeyFor(bytes);
    }

    //토큰 발급
    public String createToken(Authentication authentication){

        String authories = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date expires = new Date((new Date()).getTime() + tokenValidityInSeconds * 1000); //1시간 뒤 토큰 만료
        return Jwts.builder()
                .setSubject(authentication.getName()) //아이디,email(토큰에 포함됨)
                .claim("auth",authories)//권한 정보(토큰에 포함됨)
                .signWith(key, SignatureAlgorithm.HS512) //HMAC + SHA512.
                .setExpiration(expires)//토큰 유효시간
                .compact(); //문자열로 위의 정보들을 만들어줌
    }
    //토큰 안에 담겨 있는 이메일을 통해 회원정보 조회하기
    public Authentication getAuthentication(String token){
        //Authentication:Spring Security에서 회원정보(토큰정보)가 담겨있는 객체
        Claims claims = Jwts.parser()//토큰 분해
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getPayload();

        String email = claims.getSubject();
        MemberInfo userDetails = (MemberInfo)infoService.loadUserByUsername(email);
        String auth = (String)claims.get("auth");
        List<? extends GrantedAuthority> authorities= Arrays.stream(auth.split(","))
                .map(SimpleGrantedAuthority::new).toList();
        userDetails.setAuthorities(authorities);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
        return authentication;
    }

    public void validateToken(String token) {
        try {
            Claims claims = Jwts.parser()//토큰 분해
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new BadRequestException(Utils.getMessage("EXPIRED.JWT_TOKEN", "validation"));
        } catch (UnsupportedJwtException e) {
            throw new BadRequestException(Utils.getMessage("UNSUPPORTED.JWT_TOKEN", "validation"));
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
            throw new BadRequestException(Utils.getMessage("INVALID_FORMAT.JWT_TOKEN", "validation"));
        }
    }
}
