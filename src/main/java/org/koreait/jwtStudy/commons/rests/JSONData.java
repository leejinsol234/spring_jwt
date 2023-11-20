package org.koreait.jwtStudy.commons.rests;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class JSONData<T> {
    //MemberController에서 반환값들을 통일성있게 처리하기 위해
    private boolean success = true;
    private HttpStatus status = HttpStatus.OK;
    @NonNull
    private T data;
    private String message;

}
