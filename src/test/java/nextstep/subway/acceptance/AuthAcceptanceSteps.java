package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

public class AuthAcceptanceSteps {

    public static void 베어러_인증_로그인에_실패하면_예외_처리한다(final String email, final String password) {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(email, password);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
