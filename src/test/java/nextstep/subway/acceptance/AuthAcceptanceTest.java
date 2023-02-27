package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.subway.acceptance.AuthSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    private DataLoader dataLoader;
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";



    /**
     * Given - 회원 데이터를 생성하고
     * When - 생성된 회원 데이터로 로그인을 하면
     * Then - accessToken을 발급받을 수 있다.
     */
    @Test
    @DisplayName("일반 로그인")
    void loginTest() {
        // given
        dataLoader.loadMemberData();

        // when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }


    /**
     * Given - Github 회원이 존재하고
     * When - Github 로그인을 하면
     * Then - accessToken을 발급받을 수 있다
     */
    @ParameterizedTest
    @ValueSource(strings = {"a123a123", "b123b123"})
    @DisplayName("Github 로그인 - 회원 정보 존재")
    void githubLoginTest(String code) {
        // given
        dataLoader.loadGithubMemberData();

        // when
        ExtractableResponse<Response> response = 깃허브_권한증서_로그인_요청(code);

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * When - 가입되지 않은 회원이 로그인 요청하면
     * Then - accessToken을 발급받을 수 있다
     */
    @ParameterizedTest
    @ValueSource(strings = {"a123a123", "b123b123"})
    @DisplayName("Github 로그인 - 회원 정보 미존재")
    void githubLoginTestWithNoMember(String code) {

        // when
        ExtractableResponse<Response> response = 깃허브_권한증서_로그인_요청(code);

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}