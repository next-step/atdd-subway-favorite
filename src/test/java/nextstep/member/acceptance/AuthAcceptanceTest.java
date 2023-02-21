package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AcceptanceTest;
import nextstep.DataLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static nextstep.DataLoader.ADMIN_EMAIL;
import static nextstep.DataLoader.ADMIN_PASSWORD;
import static nextstep.member.acceptance.LoginSteps.베어러_인증_로그인_실패하는_요청;
import static nextstep.member.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {

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
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(ADMIN_EMAIL, ADMIN_PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @Test
    void wrongPassword() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_실패하는_요청(ADMIN_EMAIL, "틀린 암호");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void notExistEmail() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_실패하는_요청("존재하지 않는 이메일", ADMIN_PASSWORD);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}