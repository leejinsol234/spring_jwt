package org.koreait.jwtStudy.models.member;

import lombok.RequiredArgsConstructor;
import org.koreait.jwtStudy.api.controllers.members.JoinValidator;
import org.koreait.jwtStudy.api.controllers.members.RequestJoin;
import org.koreait.jwtStudy.commons.constant.MemberType;
import org.koreait.jwtStudy.entities.Member;
import org.koreait.jwtStudy.repositories.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
@RequiredArgsConstructor
public class MemberSaveService {

    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JoinValidator joinValidator;
    public void save(RequestJoin form, Errors errors){
        joinValidator.validate(form, errors);

        if(errors.hasErrors()){
            return;
        }

        //회원 가입 처리
        String hash = passwordEncoder.encode(form.password());
        Member member = Member.builder()
                .email(form.email())
                .name(form.name())
                .password(form.password())
                .mobile(form.mobile())
                .type(MemberType.USER)
                .build();

        save(member);
    }

    public void save(Member member){
        //휴대전화 번호에서 숫자를 뺀 특수기호 등 제외하기(필수 아니므로 null가능)
        String mobile = member.getMobile();
        if(member != null){
            mobile = mobile.replaceAll("\\D","");
            member.setMobile(mobile);
        }
        repository.saveAndFlush(member);
    }
}
