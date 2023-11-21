package org.koreait.jwtStudy.tests;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.koreait.jwtStudy.api.controllers.members.RequestJoin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class MemberJoinTest {
    @Autowired
    public MockMvc mockMvc;

    @Test
    @DisplayName("회원가입 테스트")
    void joinTest() throws Exception {
        RequestJoin form = RequestJoin.builder()
                .email("user01@test.org")
                .password("123456")
                .confirmPassword("123456")
                .name("사용자01")
                .mobile("010-1111-2222")
                .agree(true)
                .build();
        //Java(요청데이터) -> JSON 또는 JSON -> Java
        ObjectMapper om = new ObjectMapper();
        String params = om.writeValueAsString(form);

        mockMvc.perform(
                post("/api/v1/member")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(params)
                .characterEncoding("UTF-8")
                        .with(csrf().asHeader())
        ).andDo(print());
    }
}
