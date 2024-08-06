package nextstep.subway.util;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class StationStep {

    public static List<String> 지하철_역_전체_조회() {

        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);

    }

    public static List<StationResponse> 여러_개의_지하철_역_생성(List<String> stations) {
        List<StationResponse> createStationResponseList = new ArrayList<>();
        for (String station : stations) {
            StationResponse createStationResponse = 지하철_역_등록(station);
            createStationResponseList.add(createStationResponse);
        }
        createStationResponseList.sort(Comparator.comparingLong(StationResponse::getId));

        return createStationResponseList;
    }

    public static StationResponse 지하철_역_등록(String stationName) {
        Map<String, String> createdStationParams = new HashMap<>();
        createdStationParams.put("name", stationName);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(createdStationParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        Long stationId_response = response.body().jsonPath().getLong("id");
        String stationName_response = response.body().jsonPath().getString("name");

        return StationResponse.of(stationId_response, stationName_response);
    }


    public static void 지하철_역_삭제(Long stationIdx) {

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/" + stationIdx)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}

