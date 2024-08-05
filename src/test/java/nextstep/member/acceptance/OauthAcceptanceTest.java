package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.dto.AccessTokenRequest;
import nextstep.utils.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OauthAcceptanceTest extends AcceptanceTest {

    @DisplayName("인증 코드가 잘못된 경우 에러 응답을 반환합니다.")
    @Test
    void invalidCode() {
        // given
        String code = "asdfajsdkfjskldjkflj";
        // when
        ExtractableResponse<Response> result = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new AccessTokenRequest(code))
                .when()
                .post("/login/github")
                .then().extract();
        // then0
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
