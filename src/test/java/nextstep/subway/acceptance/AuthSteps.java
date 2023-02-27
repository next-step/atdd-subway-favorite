package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;


public class AuthSteps {

    public static void Access_Token_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getString("accessToken")).isNotNull();
    }
}
