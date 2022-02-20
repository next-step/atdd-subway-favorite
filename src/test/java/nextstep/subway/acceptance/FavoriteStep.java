package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteStep {

    public static ExtractableResponse<Response> 즐겨찾기_생성(String 액세스_토큰, Long 교대역, Long 강남역) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", 교대역);
        params.put("target", 강남역);

        return RestAssured
                .given().log().all()
                .header(HttpHeaders.AUTHORIZATION,"Bearer "+액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/favorites ")
                .then().log().all().extract();
    }
}
