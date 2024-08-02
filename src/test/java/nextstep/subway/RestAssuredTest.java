package nextstep.subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestAssuredTest {

    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {
        String targetURL = "https://google.com";

        RestAssured.baseURI = targetURL;

        ExtractableResponse<Response> response = given()
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}