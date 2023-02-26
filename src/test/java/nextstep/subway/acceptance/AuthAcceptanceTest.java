package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.DataLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
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
    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        // given
        dataLoader.loadMemberData();

        // when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}