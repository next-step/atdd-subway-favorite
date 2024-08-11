package nextstep.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.line.acceptance.LineSteps.*;
import static nextstep.station.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;



@DisplayName("지하철 구간 관리 기능")
class LineSectionAcceptanceTest extends AcceptanceTest {
    private Long 신분당선;

    private Long 강남역;
    private Long 양재역;
    private Long 정자역;

    /**
     * Given 지하철역과 노선 생성을 요청 하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        정자역 = 지하철역_생성_요청("정자역").jsonPath().getLong("id");

        Map<String, String> lineCreateParams = createLineCreateParams(강남역, 양재역);
        신분당선 = 지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 정자역));


    }

    /**
     * When 지하철 노선 끝에 새로운 구간 추가를 요청 하면
     * Then 노선에 새로운 구간이 추가된다
     */
    @DisplayName("지하철 노선에 구간을 등록")
    @Test
    void addLineSection() {
        // when
        Long 광교역 = 지하철역_생성_요청("광교역").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(정자역, 광교역));

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(강남역, 양재역, 정자역, 광교역);
    }

    /**
     * When 지하철 노선 가운데에 역(구간) 추가를 요청하면
     * Then 노선에 새로운 구간이 추가된다
     */
    @DisplayName("노선에 역 추가시 노선 가운데 추가 할 수 있다.")
    @Test
    void addLineMiddleSection() {
        // when
        Long 판교역 = 지하철역_생성_요청("판교역").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 판교역, 3L));

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(강남역, 양재역, 판교역, 정자역);
    }

    /**
     * When 지하철 노선 처음에 역(구간) 추가를 요청하면
     * Then 노선에 새로운 구간이 추가된다
     */
    @DisplayName("노선에 역 추가시 노선 처음에 추가 할 수 있다.")
    @Test
    void addLineFirstSection() {
        // when
        Long 신사역 = 지하철역_생성_요청("신사역").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(신사역, 강남역));

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(신사역, 강남역, 양재역, 정자역);
    }

    /**
     * When 지하철 노선에 이미 등록되어 있는 역(구간) 추가를 요청하면
     * Then 예외가 발생한다.
     */
    @DisplayName("이미 등록되어있는 역은 노선에 등록될 수 없다.")
    @Test
    void addLineExistSection() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 정자역));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("code")).isEqualTo("S005");
        assertThat(response.jsonPath().getString("message")).isEqualTo(" 해당 역이 이미 노선에 포함되어 있습니다.");

    }

    /**
     * When 지하철 노선에 거리가 1보다 작은 구간을 추가하면
     * Then 예외가 발생한다.
     */
    @DisplayName("추가되는 구간의 거리는 1이상이어야 한다.")
    @Test
    void addLineSectionDistanceException() {
        // when
        Long 광교역 = 지하철역_생성_요청("광교역").jsonPath().getLong("id");
        ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(정자역, 광교역,0L));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("code")).isEqualTo("S007");
        assertThat(response.jsonPath().getString("message")).isEqualTo(" 추가되는 구간의 거리는 1이상이어야 합니다.");

    }

    /**
     * When 지하철 노선에 기존 구간의 거리보다 큰 거리의 구간을 추가하면
     * Then 예외가 발생한다.
     */
    @DisplayName("추가되는 구간의 거리는 기존 구간의 거리보다 작아야한다.")
    @Test
    void addLineSectionDistanceTooLongException() {
        // when
        Long 판교역 = 지하철역_생성_요청("판교역").jsonPath().getLong("id");
        ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 판교역, 10L));
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("code")).isEqualTo("S006");
        assertThat(response.jsonPath().getString("message")).isEqualTo(" 추가되는 구간의 거리는 기존 구간의 거리보다 작아야합니다.");

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
        var 정자역 = 지하철역_생성_요청("정자역").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 정자역));

        // when
        지하철_노선에_지하철_구간_제거_요청(신분당선, 정자역);

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(강남역, 양재역);
    }

    /**
     * When 지하철 노선의 처음 구간 제거를 요청 하면
     * Then 노선에 구간이 제거된다
     */
    @DisplayName("노선에 등록된 역 제거 시 해당 역이 노선 가운데 있어도 제거할 수 있다.")
    @Test
    void removeLineMiddleSection() {

        // when
        지하철_노선에_지하철_구간_제거_요청(신분당선, 양재역);

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(강남역, 정자역);
    }


    /**
     * When 지하철 노선의 처음 구간 제거를 요청 하면
     * Then 노선에 구간이 제거된다
     */
    @DisplayName("노선에 등록된 역 제거 시 해당 역이 상행 종점역이어도 제거할 수 있다.")
    @Test
    void removeLineFirstSection() {

        // when
        지하철_노선에_지하철_구간_제거_요청(신분당선, 강남역);

        // then
        var response = 지하철_노선_조회_요청(신분당선);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(양재역, 정자역);
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

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, Long distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        return params;
    }
}
