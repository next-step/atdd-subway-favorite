package nextstep.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.domain.member.Member;
import nextstep.domain.member.MemberRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestAssuredTest {

    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {
        ExtractableResponse<Response> response = RestAssured.given()
                .when()
                .get("https://google.com")
                .then()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    static class AuthAcceptanceTest extends AcceptanceTest {
        public static final String EMAIL = "admin@email.com";
        public static final String PASSWORD = "password";
        public static final Integer AGE = 20;

        @Autowired
        private MemberRepository memberRepository;

        @DisplayName("Bearer Auth")
        @Test
        void bearerAuth() {
            memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

            Map<String, String> params = new HashMap<>();
            params.put("email", EMAIL);
            params.put("password", PASSWORD);

            ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(params)
                    .when().post("/login/token")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value()).extract();

            assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
        }

        @DisplayName("Github Auth")
        @Test
        void githubAuth() {
            Map<String, String> params = new HashMap<>();
            params.put("code", "code");

            ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(params)
                    .when().post("/login/github")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value()).extract();

            assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
        }
    }
}