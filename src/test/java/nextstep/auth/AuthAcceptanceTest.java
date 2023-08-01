package nextstep.auth;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import nextstep.auth.github.VirtualUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static nextstep.auth.AuthSteps.*;

class AuthAcceptanceTest extends AcceptanceTest {
    private final VirtualUser properUser = VirtualUser.사용자1;
    private final String LOCAL_ADDRESS = "http://localhost:8080";


    @Value("${github.url.access-token}")
    private String tokenUrl;

    @Value("${github.url.profile}")
    private String profileUrl;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        super.setUp();
        tokenUrl = tokenUrl.replace(LOCAL_ADDRESS, "");
        profileUrl = profileUrl.replace(LOCAL_ADDRESS, "");

    }

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

    @Nested
    @DisplayName("깃허브 Oauth2 로그인 성공하는 시나리오엔")
    class GithubAuthSuccess {
        @DisplayName("서버로 리다이렉트 되면 클라이언트는 엑세스 토큰을 받는 시나리오")
        @Test
        void githubAuth() {
            // when
            ExtractableResponse<Response> response = 깃허브_로그인_요청(properUser.getCode());

            // then
            access_token_응답을_받음(response);
        }

        @DisplayName("인증 서버에 토큰 요청(with code)하면 토큰을 응답 받는 시나리오")
        @Test
        void githubAuthPostForAccessToken() {
            // when
            ExtractableResponse<Response> response = oauth2_깃허브에_토큰_요청(tokenUrl, properUser.getCode());

            // then
            oauth2_깃허브_토큰_응답_받음(response);
        }

        @DisplayName("리소스 서버에 리소스 조회 요(with token)하면 리소스를 정상 응답 받는 시나리오")
        @Test
        void githubAuthGetUserInfo() {
            // when
            ExtractableResponse<Response> response = oauth2_깃허브_리소스_조회_요청(profileUrl, properUser.getToken());

            // then
            이메일_응답_받음(response, "email");
        }
    }
}