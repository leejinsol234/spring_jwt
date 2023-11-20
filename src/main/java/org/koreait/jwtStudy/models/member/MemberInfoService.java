package org.koreait.jwtStudy.models.member;

import lombok.RequiredArgsConstructor;
import org.koreait.jwtStudy.commons.constant.MemberType;
import org.koreait.jwtStudy.entities.Member;
import org.koreait.jwtStudy.repositories.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {

    private final MemberRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = repository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException(username));

        MemberType type = Objects.requireNonNullElse(member.getType(), MemberType.USER);

        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(type.name()));

        return MemberInfo.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .authorities(authorities)
                .member(member)
                .build();
    }
}
