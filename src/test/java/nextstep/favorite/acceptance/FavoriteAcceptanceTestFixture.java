package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.line.domain.Line;
import nextstep.station.domain.Station;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTestFixture extends AcceptanceTest {

    public static String 로그인(String email, String password) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of("email", email, "password", password))
                .when().post("/login/token")
                .then().log().all()
                .extract();
        return response.jsonPath().getString("accessToken");
    }

    public static Line 신분당선(Station upStation, Station downStation) {
        return Line.builder()
                .id(1L)
                .name("신분당선")
                .color("red")
                .upStation(upStation)
                .downStation(downStation)
                .distance(10)
                .build();
    }

    public static Line 이호선(Station upStation, Station downStation) {
        return Line.builder()
                .id(2L)
                .name("이호선")
                .color("blue")
                .upStation(upStation)
                .downStation(downStation)
                .distance(9)
                .build();
    }

    public static ExtractableResponse<Response> 즐겨찾기_추가_요청(String token, Long source, Long target) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .body(Map.of("source", source, "target", target))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, Long id) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when().delete(String.format("/favorites/%s", id))
                .then().log().all()
                .extract();
    }

    public static List<String> getStationNames(ExtractableResponse<Response> response) {
        List<String> names = new ArrayList<>();
        List<String> sourceNames = response.jsonPath().getList("source.name", String.class);
        List<String> targetNames = response.jsonPath().getList("target.name", String.class);
        names.addAll(sourceNames);
        names.addAll(targetNames);
        return names;
    }
}