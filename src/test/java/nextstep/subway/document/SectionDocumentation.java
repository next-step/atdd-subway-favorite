package nextstep.subway.document;

import io.restassured.RestAssured;
import nextstep.subway.Documentation;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.station.acceptance.StationSteps.지하철역_등록되어_있음;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class SectionDocumentation extends Documentation {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);

        강남역 = 지하철역_등록되어_있음(로그인_사용자, "강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음(로그인_사용자, "양재역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음(로그인_사용자, "정자역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음(로그인_사용자, "광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 10);
        신분당선 = 지하철_노선_등록되어_있음(로그인_사용자, lineRequest).as(LineResponse.class);
    }

    @Test
    void section() {
        SectionRequest sectionRequest = new SectionRequest(양재역.getId(), 정자역.getId(), 6, 10);

        RestAssured
                .given(spec).log().all()
                .filter(document("section/add",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .header("Authorization", "Bearer " + 로그인_사용자.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .pathParam("lineId", 신분당선.getId())
                .when().post("/lines/{lineId}/sections")
                .then().log().all().extract();

        RestAssured
                .given(spec).log().all()
                .filter(document("section/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .header("Authorization", "Bearer " + 로그인_사용자.getAccessToken())
                .pathParam("lineId", 신분당선.getId())
                .queryParam("stationId", 양재역.getId())
                .when().delete("/lines/{lineId}/sections")
                .then().log().all().extract();
    }
}
