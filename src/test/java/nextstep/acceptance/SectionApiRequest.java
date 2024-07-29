package nextstep.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import nextstep.line.payload.AddSectionRequest;
import org.springframework.http.MediaType;

import java.util.Map;

public class SectionApiRequest {

    public static Response 구간을_추가한다(final Long 노선, final AddSectionRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(String.format("/lines/%d/sections", 노선))
                .then().log().all()
                .extract().response();
    }

    public static Response 노선에서_역을_삭제한다(final Long 노선, final Long 역) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .params(Map.of("stationId", 역))
                .when().delete(String.format("/lines/%d/sections", 노선))
                .then().log().all()
                .extract().response();
    }

}
