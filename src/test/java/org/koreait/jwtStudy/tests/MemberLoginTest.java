package org.koreait.jwtStudy.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.koreait.jwtStudy.api.controllers.members.RequestLogin;
import org.koreait.jwtStudy.commons.constant.MemberType;
import org.koreait.jwtStudy.commons.rests.JSONData;
import org.koreait.jwtStudy.entities.Member;
import org.koreait.jwtStudy.models.member.MemberSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Transactional
public class MemberLoginTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberSaveService memberSaveService;
    @Autowired
    private PasswordEncoder encoder;

    private Member member;

    @BeforeEach
    void init(){
        member = Member.builder()
                .email("user01@test.org")
                .password(encoder.encode("_aA123456"))
                .name("사용자01")
                .mobile("010-1234-5678")
                .type(MemberType.ADMIN)
                .build();
        memberSaveService.save(member);
    }

    @Test
    @DisplayName("로그인 시 토큰 발급 되는지 확인")
    void loginTest() throws Exception {

        RequestLogin form = RequestLogin.builder()
                .email(member.getEmail())
                .password("_aA123456")
                .build();
        ObjectMapper om = new ObjectMapper();
        String params = om.writeValueAsString(form);
        String body = mockMvc.perform(
                post("/api/v1/member/token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(params)
        ).andDo(print())
                .andReturn().getResponse().getContentAsString(Charset.forName("UTF-8"));

        JSONData data = om.readValue(body, JSONData.class);
        String accessToken = (String)data.getData();

        mockMvc.perform(get("/api/v1/member/admin")
                .header("Authorization", "Bearer "+accessToken)
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("미로그인 상태(토큰이 없는)시 회원 전용 URL 접근 통제 테스트")
    void guestAccessTest() throws Exception {
        mockMvc.perform(get("/api/v1/member/info"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}
