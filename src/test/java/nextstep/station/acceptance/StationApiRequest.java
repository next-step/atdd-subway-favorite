package nextstep.station.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

public class StationApiRequest {

    private static final String URI_PREFIX = "/stations";

    public static Response 역을_생성한다(String name) {
        return RestAssured.given().log().all()
                .body(Map.of("name", name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(URI_PREFIX)
                .then().log().all()
                .extract().response();
    }

    public static List<String> 역을_모두_조회한다() {
        return RestAssured.given().log().all()
                .when().get(URI_PREFIX)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList("name", String.class);
    }

    public static Response 역을_삭제한다(final String location) {
        return RestAssured.given().log().all()
                .when().delete(location)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract().response();
    }

}
