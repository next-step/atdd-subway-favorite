package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@DisplayName("즐겨찾기 기능을 관리한다.")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    @DisplayName("즐겨찾기 생성")
    @Test
    void create() {
        // given
        Map<String, String> param = new HashMap<>();
        param.put("source", "1");
        param.put("target", "3");

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(param);

        // then
        즐겨찾기_생성됨(response);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(Map<String, String> param) {
        String uri = "/favorites";
        return RestAssured.given().log().all()
                          .body(param)
                          .when()
                          .post(uri)
                          .then().log().all()
                          .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
