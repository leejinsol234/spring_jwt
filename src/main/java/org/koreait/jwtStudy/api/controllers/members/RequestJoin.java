package org.koreait.jwtStudy.api.controllers.members;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RequestJoin(
        //컨트롤러로부터 회원 가입 데이터 받아오기
        @NotBlank @Email
        String email,
        @NotBlank @Size(min=8)
        String password,
        @NotBlank
        String confirmPassword,
        @NotBlank
        String name,
        String mobile,
        @AssertTrue //true 검증
        Boolean agree
) {}
