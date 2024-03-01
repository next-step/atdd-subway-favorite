package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static nextstep.member.acceptance.AuthSteps.*;
import static nextstep.member.acceptance.MemberSteps.내_정보_요청;
import static nextstep.member.acceptance.MemberSteps.토큰_없이_내_정보_요청;
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
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        String accessToken = 토큰_생성(EMAIL, PASSWORD);

        assertThat(accessToken).isNotBlank();

        ExtractableResponse<Response> info = 내_정보_요청(accessToken);

        assertThat(info.jsonPath().getString("email")).isEqualTo(EMAIL);
    }

    @DisplayName("Validate Bearer Auth Without Token")
    @Test
    void validateBearerAuthWithoutToken() {
        ExtractableResponse<Response> response = 토큰_없이_내_정보_요청();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}