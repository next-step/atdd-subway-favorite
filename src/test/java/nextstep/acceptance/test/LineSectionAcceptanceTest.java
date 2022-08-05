package nextstep.acceptance.test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.acceptance.step.LineSteps;
import nextstep.acceptance.step.StationSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.acceptance.step.AuthSteps.givenUserRole;
import static nextstep.acceptance.step.AuthSteps.권한검사에_실패한다;
import static nextstep.acceptance.step.LineSteps.지하철_노선_생성_요청;
import static nextstep.acceptance.step.LineSteps.지하철_노선_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관리 기능")
class LineSectionAcceptanceTest extends AcceptanceTest {
    private Long 신분당선;

    private Long 강남역;
    private Long 양재역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationSteps.지하철역_생성("강남역").jsonPath().getLong("id");
        양재역 = StationSteps.지하철역_생성("양재역").jsonPath().getLong("id");

        Map<String, String> lineCreateParams = createLineCreateParams(강남역, 양재역);
        신분당선 = LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }

    @DisplayName("관리자는 지하철 노선 마지막에 구간을 추가할 수 있다.")
    @Test
    void addLineSection() {
        // when
        Long 정자역 = StationSteps.지하철역_생성("정자역").jsonPath().getLong("id");
        LineSteps.지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 정자역));

        // then
        지하철_노선에_구간들이_존재한다(신분당선, 강남역, 양재역, 정자역);
    }

    @DisplayName("관리자는 지하철 노선 가운데에 구간을 추가할 수 있다.")
    @Test
    void addLineSectionMiddle() {
        // when
        Long 정자역 = StationSteps.지하철역_생성("정자역").jsonPath().getLong("id");
        LineSteps.지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(강남역, 정자역));

        // then
        지하철_노선에_구간들이_존재한다(신분당선, 강남역, 정자역, 양재역);
    }

    @DisplayName("이미 존재하는 구간을 추가할 수 없다.")
    @Test
    void addSectionAlreadyIncluded() {
        // when
        var response = LineSteps.지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(강남역, 양재역));

        // then
        지하철_노선_추가에_실패한다(response);
    }

    @DisplayName("일반 사용자는 구간을 추가할 수 없다.")
    @Test
    void addSection_Unauthorized() {
        // when
        var response = 일반사용자_권한으로_지하철_노선에_지하철_구간_생성(신분당선, createSectionCreateParams(강남역, 양재역));

        // then
        권한검사에_실패한다(response);
    }

    @DisplayName("관리자는 지하철 노선의 마지막 구간을 제거할 수 있다.")
    @Test
    void removeLineSection() {
        // given
        Long 정자역 = StationSteps.지하철역_생성("정자역").jsonPath().getLong("id");
        LineSteps.지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 정자역));

        // when
        LineSteps.지하철_노선에_지하철_구간_제거_요청(신분당선, 정자역);

        // then
        지하철_노선에_구간들이_존재한다(신분당선, 강남역, 양재역);
    }

    @DisplayName("관리자는 지하철 노선의 가운데 구간을 제거할 수 있다.")
    @Test
    void removeLineSectionInMiddle() {
        // given
        Long 정자역 = StationSteps.지하철역_생성("정자역").jsonPath().getLong("id");
        LineSteps.지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 정자역));

        // when
        LineSteps.지하철_노선에_지하철_구간_제거_요청(신분당선, 양재역);

        // then
        지하철_노선에_구간들이_존재한다(신분당선, 강남역, 정자역);
    }

    @DisplayName("일반사용자는 지하철 노선을 제거할 수 없다.")
    @Test
    void removeLineSection_Unauthorized() {
        // given
        Long 정자역 = StationSteps.지하철역_생성("정자역").jsonPath().getLong("id");
        LineSteps.지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 정자역));

        // when
        var deleteResponse = 일반사용자_권한으로_지하철_노선에_지하철_구간_제거(신분당선, 양재역);

        // then
        권한검사에_실패한다(deleteResponse);
    }

    private ExtractableResponse<Response> 일반사용자_권한으로_지하철_노선에_지하철_구간_생성(Long lineId, Map<String, String> params) {
        return givenUserRole()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 일반사용자_권한으로_지하철_노선에_지하철_구간_제거(Long lineId, Long stationId) {
        return givenUserRole()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then().log().all()
                .extract();
    }

    private void 지하철_노선에_구간들이_존재한다(Long lineId, Long... stationIds) {
        var response = LineSteps.지하철_노선_조회_요청(lineId);

        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationIds);
    }

    private void 지하철_노선_추가에_실패한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private Map<String, String> createLineCreateParams(Long upStationId, Long downStationId) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", "신분당선");
        lineCreateParams.put("color", "bg-red-600");
        lineCreateParams.put("upStationId", upStationId + "");
        lineCreateParams.put("downStationId", downStationId + "");
        lineCreateParams.put("distance", 10 + "");
        return lineCreateParams;
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", 6 + "");
        return params;
    }
}
