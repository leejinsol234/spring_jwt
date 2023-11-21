package org.koreait.jwtStudy.api.controllers.members;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.koreait.jwtStudy.commons.Utils;
import org.koreait.jwtStudy.commons.exceptions.BadRequestException;
import org.koreait.jwtStudy.commons.rests.JSONData;
import org.koreait.jwtStudy.models.member.MemberSaveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //JSON형태로만 응답하도록
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberSaveService saveService;

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
}
