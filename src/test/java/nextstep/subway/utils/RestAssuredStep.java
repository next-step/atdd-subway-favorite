package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author a1101466 on 2022/07/30
 * @project subway
 * @description
 */
public class RestAssuredStep {
    public static RequestSpecification given(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken);
    }

    public static void 검증_NO_CONTENT(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
    public static void 검증_UNAUTHORIZED(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 검증_STATUS_OK(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
