package nextstep.member.acceptance;

import static nextstep.member.acceptance.AuthSteps.*;
import static nextstep.member.application.dto.GithubResponses.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;

class AuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        // given
        memberRepository.save(new Member(사용자1.getEmail(), DEFAULT_PASSWORD, DEFAULT_AGE));

        Map<String, String> params = new HashMap<>();
        params.put("email", 사용자1.getEmail());
        params.put("password", DEFAULT_PASSWORD);

        // when
        ExtractableResponse<Response> 로그인응답 = 로그인_요청(params);

        // then
        String accessToken = 로그인응답.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();

        // when
        ExtractableResponse<Response> 회원정보응답 = 회원_정보_조회_요청(accessToken);

        // then
        assertThat(회원정보응답.jsonPath().getString("email")).isEqualTo(사용자1.getEmail());
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 토큰을 생성하려고 하면 오류가 발생한다")
    void createToken_MemberNotFound() {
        Map<String, String> params = new HashMap<>();

        params.put("email", PROFILE_없는_사용자.getEmail());
        params.put("password", DEFAULT_PASSWORD);

        ExtractableResponse<Response> response = 로그인_요청(params);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("틀린 비밀번호로 토큰 생성을 요청하면 오류가 발생한다")
    void createToken_InvalidPassword() {
        memberRepository.save(new Member(사용자2.getEmail(), DEFAULT_PASSWORD, DEFAULT_AGE));

        Map<String, String> params = new HashMap<>();
        params.put("email", 사용자2.getEmail());
        params.put("password", WRONG_PASSWORD);

        ExtractableResponse<Response> response = 로그인_요청(params);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("Github 로그인을 통해 토큰을 발급받는다.")
    void githubAuth() {
        memberRepository.save(new Member(사용자3.getEmail(), DEFAULT_PASSWORD, DEFAULT_AGE));

        Map<String, String> params = new HashMap<>();
        params.put("code", 사용자3.getCode());

        ExtractableResponse<Response> response = github_로그인_요청(params);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @Test
    @DisplayName("github를 통해 토큰을 생성하려고 할 때 존재하지 않는 회원에 대해 회원이 가입되고 토큰이 생성된다")
    void createTokenFromGithub_NewMember() {
        String code = PROFILE_없는_사용자.getCode();
        String email = PROFILE_없는_사용자.getEmail();

        Map<String, String> params = new HashMap<>();
        params.put("code", code);

        ExtractableResponse<Response> response = github_로그인_요청(params);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();

        Member member = memberRepository.findByEmail(email).orElseThrow();
        assertThat(member).isNotNull();
        assertThat(member.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("잘못된 코드 값으로 Github를 통해 토큰을 생성하려고 할 때 오류가 발생한다")
    void createTokenFromGithub_InvalidAccessToken() {
        Map<String, String> params = new HashMap<>();
        params.put("code", CODE_없는_사용자.getCode());

        ExtractableResponse<Response> response = github_로그인_요청(params);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}