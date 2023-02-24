package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private DataLoader dataLoader;

    @BeforeEach
    public void setUp() {
        super.setUp();
        dataLoader.loadData();
    }

    /**
     * When 로그인을 요청하면
     * Then 토큰을 발급 받는다
     */
    @DisplayName("Bearer Auth Token 발급")
    @Test
    void bearerAuth() {
        // when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * When 잘못된 Id나 Password 를 입력하면
     * Then 오류가 발생한다
     */
    @DisplayName("잘못된 유저 정보 입력")
    @Test
    void bearerAuth_WithInvalidUserInfo() {
        // when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청("a", "b");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }
}
