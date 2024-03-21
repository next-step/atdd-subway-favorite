package nextstep.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import nextstep.utils.fixture.GithubUserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static nextstep.utils.api.AuthApi.깃헙_로그인으로_토큰_요청;
import static nextstep.utils.api.AuthApi.로그인으로_토큰_요청;
import static nextstep.utils.api.MemberApi.내_정보_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    /**
     * Given 이미 존재하는 회원이
     * When 올바른 이메일과 비밀번호로 로그인을 시도하면
     * Then 내 정보 조회가 가능하다.
     */
    @DisplayName("Bearer 토큰 발급")
    @Test
    void bearerAuth() {
        // Given
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        // When
        String 액세스_토큰 = 로그인으로_토큰_요청(EMAIL, PASSWORD);
        assertThat(액세스_토큰).isNotBlank();

        // Then
        ExtractableResponse<Response> 내_정보_조회_요청_결과 = 내_정보_조회_요청(액세스_토큰);
        assertThat(내_정보_조회_요청_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(내_정보_조회_요청_결과.jsonPath().getString("email")).isEqualTo(EMAIL);
    }

    /**
     * GIVEN 원래 멤버가 아닌 "사용자1"이
     * WHEN 깃헙 코드로 로그인을 시도하면
     * THEN 내 정보 조회가 가능하다.
     */
    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        // When
        String 액세스_토큰 = 깃헙_로그인으로_토큰_요청(GithubUserFixture.사용자1.getCode());

        ExtractableResponse<Response> 내_정보_조회_요청_결과 = 내_정보_조회_요청(액세스_토큰);

        assertThat(내_정보_조회_요청_결과.jsonPath().getString("email")).isEqualTo(GithubUserFixture.사용자1.getEmail());
    }
}
