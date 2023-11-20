package org.koreait.jwtStudy.api.controllers.members;

import jakarta.validation.Valid;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //JSON형태로만 응답하도록
@RequestMapping("/api/v1/member")
public class MemberController {

    @PostMapping() //회원가입 처리
    public void join(@RequestBody @Valid RequestJoin form, Errors errors){ //@RequestBody: JSON으로 인식해서 처리

    }
}
