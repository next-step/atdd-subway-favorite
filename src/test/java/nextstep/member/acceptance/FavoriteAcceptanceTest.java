package nextstep.member.acceptance;

import static nextstep.member.MemberSteps.*;
import static nextstep.member.FavoriteSteps.*;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.AcceptanceTest;
import nextstep.subway.acceptance.LineSteps;

@DisplayName("즐겨찾기 관리")
public class FavoriteAcceptanceTest extends AcceptanceTest {
	public static final String EMAIL = "email@email.com";
	public static final String PASSWORD = "password";
	public static final int AGE = 20;

	private String 로그인_토큰;

	private Long 교대역;
	private Long 강남역;
	private Long 양재역;
	private Long 남부터미널역;
	private Long 이호선;
	private Long 신분당선;
	private Long 삼호선;

	/**
	 *   Background
	 *     Given 지하철역 등록되어 있음
	 *     And 지하철 노선 등록되어 있음
	 *     And 지하철 노선에 지하철역 등록되어 있음
	 *     And 회원 등록되어 있음
	 *     And 로그인 되어있음
	*/
	@BeforeEach
	public void setUp() {
		super.setUp();

		교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
		강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
		양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
		남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

		이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10);
		신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10);
		삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2);

		지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3));

		회원_생성_요청(EMAIL, PASSWORD, AGE);
		로그인_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);
	}

	/**
	 *   Scenario: 즐겨찾기를 관리
	 *     When 즐겨찾기 생성을 요청
	 *     Then 즐겨찾기 생성됨
	 *     When 즐겨찾기 목록 조회 요청
	 *     Then 즐겨찾기 목록 조회됨
	 *     When 즐겨찾기 삭제 요청
	 *     Then 즐겨찾기 삭제됨
	*/
	@DisplayName("즐겨찾기 생성")
	@Test
	void 즐겨찾기_생성() {
		// when
		ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(로그인_토큰, 교대역, 강남역);

		// then
		응답_확인(즐겨찾기_생성_응답, HttpStatus.CREATED);
	}

	@DisplayName("즐겨찾기 조회")
	@Test
	void 즐겨찾기_조회() {
		// given
		즐겨찾기_생성_요청(로그인_토큰, 교대역, 강남역);
		즐겨찾기_생성_요청(로그인_토큰, 양재역, 남부터미널역);

		// when
		ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(로그인_토큰);

		// then
		응답_확인(즐겨찾기_조회_응답, HttpStatus.OK);
		assertThat(즐겨찾기_조회_응답.jsonPath().getList("source.id", Long.class)).containsExactly(교대역, 양재역);
	}

	private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance) {
		Map<String, String> lineCreateParams;
		lineCreateParams = new HashMap<>();
		lineCreateParams.put("name", name);
		lineCreateParams.put("color", color);
		lineCreateParams.put("upStationId", upStation + "");
		lineCreateParams.put("downStationId", downStation + "");
		lineCreateParams.put("distance", distance + "");

		return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
	}

	private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance) {
		Map<String, String> params = new HashMap<>();
		params.put("upStationId", upStationId + "");
		params.put("downStationId", downStationId + "");
		params.put("distance", distance + "");
		return params;
	}
}
