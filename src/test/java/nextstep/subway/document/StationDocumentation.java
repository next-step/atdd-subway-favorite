package nextstep.subway.document;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.Documentation;
import nextstep.subway.station.dto.StationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class StationDocumentation extends Documentation {
    private String 지하철역 = "지하철역";

    @Test
    void station() {
        StationRequest stationRequest = new StationRequest(지하철역);
        ExtractableResponse<Response> createResponse = RestAssured
                .given(spec).log().all()
                .filter(document("station/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .header("Authorization", "Bearer "+ 로그인_사용자.getAccessToken())
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all().extract();

        RestAssured
                .given(spec).log().all()
                .filter(document("station/read",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .header("Authorization", "Bearer "+ 로그인_사용자.getAccessToken())
                .when()
                .get("/stations")
                .then().log().all().extract();

        String location = createResponse.header("Location");
        RestAssured
                .given(spec).log().all()
                .filter(document("station/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .header("Authorization", "Bearer "+ 로그인_사용자.getAccessToken())
                .when().delete(location)
                .then().log().all().extract();
    }
}