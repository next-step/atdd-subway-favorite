package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.AccountUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관리 기능")
class SectionAcceptanceTest extends AcceptanceTest {

    private String adminToken;
    private String userToken;

    private Long 신분당선;

    private Long 강남역;
    private Long 양재역;


    /**
     * Given 지하철역과 노선 생성을 요청 하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        adminToken = MemberSteps.로그인_되어_있음(AccountUtils.ADMIN_EMAIL, AccountUtils.ADMIN_PASSWORD);
        userToken = MemberSteps.로그인_되어_있음(AccountUtils.USER_EMAIL, AccountUtils.USER_PASSWORD);

        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");

        Map<String, String> lineCreateParams = createLineCreateParams(강남역, 양재역);
        신분당선 = 지하철_노선_생성_요청(lineCreateParams, adminToken).jsonPath().getLong("id");
    }

    /**
     * When 지하철 노선에 새로운 구간 추가를 요청 하면
     * Then 노선에 새로운 구간이 추가된다
     */
    @DisplayName("지하철 노선에 구간을 등록")
    @Test
    void addLineSection() {
        // when
        Long 정자역 = 지하철역_생성_요청("정자역").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 정자역), adminToken);

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
        Long 정자역 = 지하철역_생성_요청("정자역").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 정자역), adminToken);

        // when
        지하철_노선에_지하철_구간_제거_요청(신분당선, 정자역, adminToken);

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(강남역, 양재역);
    }

    @DisplayName("지하철 노선 구간 제거 - 유효하지 않은 권한으로 요청 테스트")
    @Test
    void removeLineSectionNotAuthorized() {
        // given
        Long 정자역 = 지하철역_생성_요청("정자역").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 정자역), adminToken);

        // when
        ExtractableResponse<Response> 일반_사용자_구간_제거_응답 = 일반_사용자_구간_제거_요청(신분당선, 정자역);

        // then
        assertThat(일반_사용자_구간_제거_응답.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("지하철 노선 구간 추가 - 유효하지 않은 권한으로 요청 테스트")
    @Test
    void addLineSectionNotAuthorized() {
        // when
        Long 정자역 = 지하철역_생성_요청("정자역").jsonPath().getLong("id");

        // then
        ExtractableResponse<Response> 일반_사용자_구간_추가_응답 = 일반_사용자_구간_추가_요청(신분당선, createSectionCreateParams(양재역, 정자역));
        assertThat(일반_사용자_구간_추가_응답.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    private ExtractableResponse<Response> 일반_사용자_구간_추가_요청(Long lineId, Map<String, String> params) {
        return 지하철_노선에_지하철_구간_생성_요청(lineId, params, userToken);
    }


    private ExtractableResponse<Response> 일반_사용자_구간_제거_요청(Long lineId, Long stationId) {
        return 지하철_노선에_지하철_구간_제거_요청(lineId, stationId, userToken);
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
