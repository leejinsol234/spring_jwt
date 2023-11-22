package org.koreait.jwtStudy.api.controllers.members;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.koreait.jwtStudy.commons.Utils;
import org.koreait.jwtStudy.commons.exceptions.BadRequestException;
import org.koreait.jwtStudy.commons.rests.JSONData;
import org.koreait.jwtStudy.entities.Member;
import org.koreait.jwtStudy.models.member.MemberInfo;
import org.koreait.jwtStudy.models.member.MemberLoginService;
import org.koreait.jwtStudy.models.member.MemberSaveService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController //JSON형태로만 응답하도록
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberSaveService saveService;
    private final MemberLoginService loginService;

    @PostMapping() //회원가입 처리
    public ResponseEntity<JSONData> join(@RequestBody @Valid RequestJoin form, Errors errors){
        //@RequestBody: JSON으로 인식해서 처리
        saveService.save(form,errors);

        if(errors.hasErrors()){
            throw new BadRequestException(Utils.getMessages(errors));
        }

        JSONData data = new JSONData();
        data.setStatus(HttpStatus.CREATED);

        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @PostMapping("/token")
    public ResponseEntity<JSONData> token(@RequestBody @Valid RequestLogin form, Errors errors){
        errorProcess(errors);
        String aceessToken = loginService.login(form);

        /* 조회 방법
        * 1.응답 바디 - JSONData 형식으로
        * 2.응답 헤더 - Authorization: Bearer 토큰
        * */

        JSONData data = new JSONData(aceessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer"+aceessToken);

        return ResponseEntity.status(data.getStatus()).headers(headers).body(data);
    }

    @GetMapping("/info") //회원인증 후 접근 가능
    private JSONData info(@AuthenticationPrincipal MemberInfo memberInfo){
        Member member = memberInfo.getMember();

        return new JSONData(member);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String admin(){
        return "관리자 페이지 접속";
    }

    private void errorProcess(Errors errors){
        if(errors.hasErrors()){
            throw new BadRequestException(Utils.getMessages(errors));
        }
    }
}
