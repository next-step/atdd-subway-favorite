package nextstep.study;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        // given : 선행조건 기술
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        // when : 기능 수행
        ExtractableResponse<Response> response = AuthSteps.로그인요청(EMAIL, PASSWORD);

        // then : 결과 확인
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Github Auth")
    @ParameterizedTest
    @ValueSource(strings = {"aofijeowifjaoief", "fau3nfin93dmn", "afnm93fmdodf", "fm04fndkaladmd"})
    void githubAuth(String code) {
        // given : 선행조건 기술

        // when : 기능 수행
        ExtractableResponse<Response> response = AuthSteps.깃허브_로그인요청(code);

        // then : 결과 확인
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}