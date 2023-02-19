package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.AuthAcceptanceSteps.베어러_인증_로그인에_실패하면_예외_처리한다;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.UUID;
import nextstep.DataLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("인증 관련 기능")
class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private DataLoader dataLoader;

    @Override
    public void setUp() {
        super.setUp();
        dataLoader.loadData();
    }

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * when 베어러 인증 로그인에 실패하면
     * then 예외 처리한다.
     */
    @DisplayName("베어러 인증 로그인에 실패하면 예외 처리한다.")
    @Test
    void bearerAuthLoginFail() {
        베어러_인증_로그인에_실패하면_예외_처리한다(EMAIL, PASSWORD + UUID.randomUUID());
    }
}
