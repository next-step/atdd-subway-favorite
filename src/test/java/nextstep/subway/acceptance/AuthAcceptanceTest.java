package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.fake.GithubFakeResponses;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static nextstep.subway.acceptance.AuthSteps.*;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceSetting {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    /**
     * given : 회원을 데이터를 생성 하고
     * when : 인증 로그인 요청을 하면
     * then : jwt accessToken을 받을 수 있다.
     */
    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        //given
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        //when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        //then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * when : 권한 증서로 깃허브 엑세스 토큰을 요청하면
     * then : 엑세스 토큰을 확인할 수 있다.
     */
    @ParameterizedTest
    @EnumSource(GithubFakeResponses.class)
    void getAccessToken(GithubFakeResponses fakeUser) {
        //when
        ExtractableResponse<Response> response = 깃허브_액세스_토큰_요청(fakeUser.getCode());

        //then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
        assertThat(response.jsonPath().getString("accessToken")).isEqualTo(fakeUser.getAccessToken());
    }

    /**
     * when : 엑세스 토큰으로 깃허브 프로필을 요청하면
     * then : 사용자의 프로필의 아이디와 이메일을 확인할 수 있다.
     */
    @ParameterizedTest
    @EnumSource(GithubFakeResponses.class)
    void getUsersProfile(GithubFakeResponses fakeUser) {
        //when
        ExtractableResponse<Response> response = 회원_프로필_요청(fakeUser.getAccessToken());

        //then
        assertThat(response.jsonPath().getLong("id")).isNotNull();
        assertThat(response.jsonPath().getLong("id")).isEqualTo(fakeUser.getId());
        assertThat(response.jsonPath().getString("email")).isNotBlank();
        assertThat(response.jsonPath().getString("email")).isEqualTo(fakeUser.getEmail());
    }

    /**
     * when : 엑세스 토큰으로 깃허브 로그인을 요청하면
     * then : Jwt 토큰을 얻을 수 있다.
     */
    @ParameterizedTest
    @EnumSource(GithubFakeResponses.class)
    void githubAuth(GithubFakeResponses fakeUser) {
        //when
        ExtractableResponse<Response> response = 깃허브_로그인_요청(fakeUser.getCode());

        //then
        Assertions.assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}
