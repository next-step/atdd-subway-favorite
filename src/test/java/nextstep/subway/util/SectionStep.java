package nextstep.subway.util;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.dto.ErrorResponse;
import nextstep.section.dto.SectionRequest;
import nextstep.section.dto.SectionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SectionStep {

    public static SectionResponse 지하철_구간_등록(Long lineId, SectionRequest sectionRequest) {

        SectionResponse sectionResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract().response().body().as(SectionResponse.class);

        assertThat(sectionResponse.getLineId()).isEqualTo(lineId);
        assertThat(sectionResponse.getDistance()).isEqualTo(sectionRequest.getDistance());
        assertThat(sectionResponse.getUpStationResponse().getId()).isEqualTo(sectionRequest.getUpStationId());
        assertThat(sectionResponse.getDownStationResponse().getId()).isEqualTo(sectionRequest.getDownStationId());

        return sectionResponse;
    }

    public static ErrorResponse 지하철_구간_등록_실패(Long lineId, SectionRequest sectionRequest) {

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract().as(ErrorResponse.class);
    }

    public static void 지하철_구간_삭제(Long lineId, Long stationId) {

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + lineId + "/sections?stationId=" + stationId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ErrorResponse 지하철_구간_삭제_실패(Long lineId, Long stationId) {

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + lineId + "/sections?stationId=" + stationId)
                .then().log().all()
                .extract().as(ErrorResponse.class);
    }
}

