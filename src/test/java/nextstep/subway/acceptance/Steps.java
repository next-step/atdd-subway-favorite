package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class Steps {

    protected static void 응답_코드_검증(final ExtractableResponse<Response> response, final HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }

    protected static RequestSpecification 사용자_인증_요청(final String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken);
    }
}
