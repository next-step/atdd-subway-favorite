package nextstep.auth;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import nextstep.auth.github.VirtualUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.auth.AuthSteps.*;

class AuthAcceptanceTest extends AcceptanceTest {
    private final VirtualUser properUser = VirtualUser.사용자1;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        // given
        memberRepository.save(new Member(properUser.getEmail(), properUser.getPassword(), properUser.getAge()));

        // when
        ExtractableResponse<Response> response = 토큰_로그인_요청(properUser.getEmail(), properUser.getPassword());

        // then
        access_token_응답을_받음(response);
    }

    @DisplayName("서버로 리다이렉트 되면 클라이언트는 엑세스 토큰을 받는 시나리오")
    @Test
    void githubAuth() {
        // when
        ExtractableResponse<Response> response = 깃허브_로그인_요청(properUser.getCode());

        // then
        access_token_응답을_받음(response);
    }
}