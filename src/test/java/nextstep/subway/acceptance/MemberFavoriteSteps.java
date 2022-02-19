package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.dto.FavoriteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberFavoriteSteps {

    public static ExtractableResponse<Response> 내_즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source + "");
        params.put("target", target + "");

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static void 즐겨찾기_정보_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_정보_조회됨(ExtractableResponse<Response> response, Long source, Long target) {
        assertThat(response.jsonPath().getList("id", Long.class)).isNotNull();
        assertThat(response.jsonPath().getList("source.id", Long.class)).contains(source);
        assertThat(response.jsonPath().getList("target.id", Long.class)).contains(target);
    }

    public static ExtractableResponse<Response> 내_즐겨찾기_정보_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_즐겨찾기_삭제_요청(String accessToken, String location) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete(location)
                .then().log().all().extract();
    }

    public static void 즐겨찾기_정보_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
