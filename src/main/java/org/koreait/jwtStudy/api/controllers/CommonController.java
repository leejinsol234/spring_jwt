package org.koreait.jwtStudy.api.controllers;

import org.koreait.jwtStudy.commons.exceptions.CommonException;
import org.koreait.jwtStudy.commons.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("org.koreait.jwtStudy.api.controllers")
public class CommonController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<JSONData> errorHandler(Exception e){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Object message = e.getMessage();

        if(e instanceof CommonException){
            CommonException commonException = (CommonException) e;
            status  = commonException.getStatus();
            //메세지가 여러 개인 경우
            if(commonException.getMessages() != null) message = commonException.getMessages();
        }

        //500으로 고정. 세부 예외처리는 commons.exceptions.CommonException에서
        JSONData data = new JSONData<>();
        data.setSuccess(false);
        data.setStatus(status);
        data.setMessage(message);

        e.printStackTrace();

        return ResponseEntity.status(status).body(data);
    }
}
