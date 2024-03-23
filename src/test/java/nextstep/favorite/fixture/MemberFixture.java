package nextstep.favorite.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.dto.MemberRequest;
import nextstep.auth.application.dto.TokenRequest;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberFixture {

    public static String loginMember() {
        return loginMember("email", "password");
    }

    public static String loginMember(String email, String password) {
        TokenRequest request = new TokenRequest(email, password);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/login/token")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(200);

        return response.body()
                .jsonPath()
                .getString("accessToken");
    }

    public static void joinMember() {
        joinMember("email", "password", 20);
    }

    public static void joinMember(String email, String password, int age) {
        MemberRequest request = new MemberRequest(email, password, age);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/members")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(201);
    }
}
