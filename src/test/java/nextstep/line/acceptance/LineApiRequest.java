package nextstep.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import nextstep.line.payload.CreateLineRequest;
import nextstep.line.payload.UpdateLineRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class LineApiRequest {


    public static Response 노선을_생성한다(final String name, final String color, final Long upStationId, final Long downStationId, final Long distance) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .basePath("/lines")
                .body(new CreateLineRequest(name, color, upStationId, downStationId, distance))
                .when().post()
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().response();
    }

    public static Response getLines() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .basePath("/lines")
                .when().get()
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().response();
    }

    public static Response 노선을_조회한다(final String url) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(url)
                .then().log().all()
                .extract().response();
    }

    public static Response update(final String url, final String name, final String color) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UpdateLineRequest(name, color))
                .when().patch(url)
                .then().log().all()
                .extract().response();
    }

    public static Response delete(final String url) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(url)
                .then().log().all()
                .extract().response();
    }
}
