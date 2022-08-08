package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationSteps.관리자로_지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관리 기능")
class SectionAcceptanceTest extends AcceptanceTest {
    private Long 신분당선;

    private Long 강남역;
    private Long 양재역;

    /**
     * Given 지하철역과 노선 생성을 요청 하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 관리자로_지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 관리자로_지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");

        Map<String, String> lineCreateParams = createLineCreateParams(강남역, 양재역);
        신분당선 = 관리자로_지하철_노선_생성_요청(관리자, lineCreateParams).jsonPath().getLong("id");
    }

    /**
     * When 지하철 노선에 새로운 구간 추가를 요청 하면
     * Then 노선에 새로운 구간이 추가된다
     */
    @DisplayName("지하철 노선에 구간을 등록")
    @Test
    void addLineSection() {
        // when
        Long 정자역 = 관리자로_지하철역_생성_요청(관리자, "정자역").jsonPath().getLong("id");
        관리자로_지하철_노선에_지하철_구간_생성_요청(관리자, 신분당선, createSectionCreateParams(양재역, 정자역));

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(강남역, 양재역, 정자역);
    }

    /**
     * Given 지하철 노선에 새로운 구간 추가를 요청 하고
     * When 지하철 노선의 마지막 구간 제거를 요청 하면
     * Then 노선에 구간이 제거된다
     */
    @DisplayName("지하철 노선에 구간을 제거")
    @Test
    void removeLineSection() {
        // given
        Long 정자역 = 관리자로_지하철역_생성_요청(관리자, "정자역").jsonPath().getLong("id");
        관리자로_지하철_노선에_지하철_구간_생성_요청(관리자, 신분당선, createSectionCreateParams(양재역, 정자역));

        // when
        관리자로_지하철_노선에_지하철_구간_제거_요청(관리자, 신분당선, 정자역);

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(강남역, 양재역);
    }

    /**
     * Given 지하철역이 생성되어 있고, 일반사용자가 로그인 되어 있을때,
     * When 일반사용자로 지하철 노선에 새로운 구간 추가를 요청 하면
     * Then 노선에 새로운 구간이 추가되지 않는다.
     */
    @DisplayName("일반 사용자는 지하철 노선에 구간을 추가 할 수 없음")
    @Test
    void canNotAddLineSectionWithMemberToken() {
        // given
        Long 정자역 = 관리자로_지하철역_생성_요청(관리자, "정자역").jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 관리자로_지하철_노선에_지하철_구간_생성_요청(일반사용자, 신분당선, createSectionCreateParams(양재역, 정자역));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 지하철 노선에 새로운 구간 추가를 요청 하고
     * When 일반사용자가 지하철 노선의 마지막 구간 제거를 요청 하면
     * Then 노선에 구간이 제거되지 않는다
     */
    @DisplayName("일반 사용자는 지하철 노선에 구간을 제거 할 수 없음")
    @Test
    void canNotRemoveLineSectionWithMemberToken() {
        // given
        Long 정자역 = 관리자로_지하철역_생성_요청(관리자, "정자역").jsonPath().getLong("id");
        관리자로_지하철_노선에_지하철_구간_생성_요청(관리자, 신분당선, createSectionCreateParams(양재역, 정자역));

        // when
        ExtractableResponse<Response> response = 관리자로_지하철_노선에_지하철_구간_제거_요청(일반사용자, 신분당선, 정자역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

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
