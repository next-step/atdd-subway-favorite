package nextstep.study;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static nextstep.subway.utils.AcceptanceTestUtil.상태코드_OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestAssuredTest extends AcceptanceTest {

    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {
        ExtractableResponse<Response> response = given()
                .baseUri("https://google.com")
                .when()
                .get()
                .then()
                .extract();

        상태코드_OK(response);
    }
}