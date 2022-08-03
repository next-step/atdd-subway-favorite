package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철 구간 관리 기능")
class LineSectionAcceptanceTest extends AcceptanceTest {
	private Long 신분당선;

	private Long 강남역;
	private Long 양재역;

	private String adminAccessToken;
	private String memberAccessToken;

	/**
	 * Given Admin이 지하철역과 노선 생성을 요청 하고
	 */
	@BeforeEach
	public void setUp() {
		super.setUp();
		adminAccessToken = 로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD);
		memberAccessToken = 로그인_되어_있음(MEMBER_EMAIL, MEMBER_PASSWORD);
		강남역 = 지하철역_생성_요청("강남역", adminAccessToken).jsonPath().getLong("id");
		양재역 = 지하철역_생성_요청("양재역", adminAccessToken).jsonPath().getLong("id");

		Map<String, String> lineCreateParams = createLineCreateParams(강남역, 양재역);
		신분당선 = 지하철_노선_생성_요청(lineCreateParams, adminAccessToken).jsonPath().getLong("id");
	}

	/**
	 * When Admin이 지하철 노선에 새로운 구간 추가를 요청 하면
	 * Then 노선에 새로운 구간이 추가된다
	 */
	@DisplayName("지하철 노선에 구간을 등록")
	@Test
	void addLineSection() {
		// when
		Long 정자역 = 지하철역_생성_요청("정자역", adminAccessToken).jsonPath().getLong("id");
		지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 정자역), adminAccessToken);

		// then
		ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(강남역, 양재역, 정자역);
	}

	/**
	 * When Admin이 지하철 노선 가운데에 새로운 구간 추가를 요청 하면
	 * Then 노선에 새로운 구간이 추가된다
	 */
	@DisplayName("지하철 노선 가운데에 구간을 추가")
	@Test
	void addLineSectionMiddle() {
		// when
		Long 정자역 = 지하철역_생성_요청("정자역", adminAccessToken).jsonPath().getLong("id");
		지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(강남역, 정자역), adminAccessToken);

		// then
		ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(강남역, 정자역, 양재역);
	}

	/**
	 * When Admin이 지하철 노선에 이미 존재하는 구간 추가를 요청 하면
	 * Then 노선에 새로운 구간추가를 실패한다
	 */
	@DisplayName("이미 존재하는 구간을 추가")
	@Test
	void addSectionAlreadyIncluded() {
		// when
		ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(강남역, 양재역),
			adminAccessToken);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	/**
	 * Given Admin이 지하철 노선에 새로운 구간 추가를 요청 하고
	 * When Admin이 지하철 노선의 마지막 구간 제거를 요청 하면
	 * Then 노선에 구간이 제거된다
	 */
	@DisplayName("지하철 노선의 마지막 구간을 제거")
	@Test
	void removeLineSection() {
		// given
		Long 정자역 = 지하철역_생성_요청("정자역", adminAccessToken).jsonPath().getLong("id");
		지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 정자역), adminAccessToken);

		// when
		지하철_노선에_지하철_구간_제거_요청(신분당선, 정자역, adminAccessToken);

		// then
		ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(강남역, 양재역);
	}

	/**
	 * Given Admin이 지하철 노선에 새로운 구간 추가를 요청 하고
	 * When Admin이 지하철 노선의 가운데 구간 제거를 요청 하면
	 * Then 노선에 구간이 제거된다
	 */
	@DisplayName("지하철 노선의 가운데 구간을 제거")
	@Test
	void removeLineSectionInMiddle() {
		// given
		Long 정자역 = 지하철역_생성_요청("정자역", adminAccessToken).jsonPath().getLong("id");
		지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 정자역), adminAccessToken);

		// when
		지하철_노선에_지하철_구간_제거_요청(신분당선, 양재역, adminAccessToken);

		// then
		ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(강남역, 정자역);
	}

	/**
	 * When Member가 지하철 노선에 새로운 구간 추가를 요청 하면
	 * Then 401 응답을 받는다
	 */
	@DisplayName("지하철 노선에 구간을 등록")
	@Test
	void addLineSectionByMember() {
		// when
		Long 정자역 = 지하철역_생성_요청("정자역", adminAccessToken).jsonPath().getLong("id");
		ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 정자역),
			memberAccessToken);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

	}

	/**
	 * Given Admin이 지하철 노선에 새로운 구간 추가를 요청 하고
	 * When Member가 지하철 노선의 마지막 구간 제거를 요청 하면
	 * Then 401 응답을 받는다
	 */
	@DisplayName("지하철 노선의 마지막 구간을 제거")
	@Test
	void removeLineSectionByMember() {
		// given
		Long 정자역 = 지하철역_생성_요청("정자역", adminAccessToken).jsonPath().getLong("id");
		지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 정자역), adminAccessToken);

		// when
		ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_제거_요청(신분당선, 정자역, memberAccessToken);

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
