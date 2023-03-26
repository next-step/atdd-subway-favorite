package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.AuthSteps.*;
import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

class FavoriteAcceptanceTest extends AcceptanceTest {
	private static final String UNAUTHORIZED_TOKEN = "unauthorized";
	private static final String EMAIL = "admin@email.com";
	private static final String PASSWORD = "password";
	private static final int AGE = 28;

	private Long 신분당선;

	private Long 강남역;
	private Long 양재역;

	@BeforeEach
	public void setUp() {
		super.setUp();
		// Given 지하철 역 2개가 주어지고
		강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
		양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");

		Map<String, String> lineCreateParams = createLineCreateParams(강남역, 양재역);
		신분당선 = 지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
	}

	/**
	 * Given 로그인 회원이 주어지고
	 * When 즐겨찾기를 생성하면
	 * Then 즐겨찾기 목록 조회 시, 생성한 즐겨찾기 항목을 찾을 수 있다.
	 */
	@DisplayName("로그인 유저의 즐겨찾기 생성")
	@Test
	void createFavorite_LOGIN_USER() {
		// Given
		String token = 회원_생성과_베어러_인증_로그인_요청(EMAIL, PASSWORD, AGE);
		// When
		ExtractableResponse<Response> createResponse = 로그인_회원의_즐겨찾기_생성_요청(token, 강남역, 양재역);
		// Then
		즐겨찾기_생성_검증(createResponse);
		ExtractableResponse<Response> response = 로그인_회원의_즐겨찾기_목록_조회_요청(token);
		즐겨찾기_목록_조회_검증(response, List.of(강남역), List.of(양재역));
	}

	/**
	 * Given 로그인 회원이 2개의 즐겨찾기를 생성하고
	 * When 즐겨찾기 목록 조회를 하면
	 * Then 2개의 즐겨찾기 항목을 조회할 수 있다.
	 */
	@DisplayName("로그인 유저의 즐겨찾기 목록 조회")
	@Test
	void getFavorites_LOGIN_USER() {
		// Given
		String token = 회원_생성과_베어러_인증_로그인_요청(EMAIL, PASSWORD, AGE);
		// When
		로그인_회원의_즐겨찾기_생성_요청(token, 강남역, 양재역);
		로그인_회원의_즐겨찾기_생성_요청(token, 양재역, 강남역);
		// Then
		ExtractableResponse<Response> response = 로그인_회원의_즐겨찾기_목록_조회_요청(token);
		즐겨찾기_목록_조회_검증(response, List.of(강남역, 양재역), List.of(양재역, 강남역));
	}

	/**
	 * Given 로그인 회원이 즐겨찾기를 생성하고
	 * When 즐겨찾기를 삭제하면
	 * Then 즐겨찾기 항목이 삭제된다.
	 */
	@DisplayName("로그인 유저의 즐겨찾기 삭제")
	@Test
	void deleteFavorite_LOGIN_USER() {
		// Given
		String token = 회원_생성과_베어러_인증_로그인_요청(EMAIL, PASSWORD, AGE);
		로그인_회원의_즐겨찾기_생성_요청(token, 강남역, 양재역);
		// When
		ExtractableResponse<Response> response = 로그인_회원의_즐겨찾기_제거_요청(token, 1L);
		// Then
		즐겨찾기_삭제_검증(response);
	}

	/**
	 * When 즐겨찾기를 생성하면
	 * Then UnAuthorized 예외 응답을 받는다.
	 */
	@DisplayName("비로그인 유저의 즐겨찾기 생성")
	@Test
	void createFavorite_NO_LOGIN_USER() {
		// When
		ExtractableResponse<Response> createResponse = 로그인_회원의_즐겨찾기_생성_요청(UNAUTHORIZED_TOKEN, 강남역, 양재역);
		// Then
		권한이_없음을_검증(createResponse);
	}

	/**
	 * Given 2개의 즐겨찾기를 생성하고
	 * When 즐겨찾기 목록 조회를 하면
	 * Then UnAuthorized 예외 응답을 받는다.
	 */
	@DisplayName("비로그인 유저의 즐겨찾기 목록 조회")
	@Test
	void getFavorites_NO_LOGIN_USER() {
		// Given
		String token = 회원_생성과_베어러_인증_로그인_요청(EMAIL, PASSWORD, AGE);
		로그인_회원의_즐겨찾기_생성_요청(token, 강남역, 양재역);
		// When
		ExtractableResponse<Response> response = 로그인_회원의_즐겨찾기_목록_조회_요청(UNAUTHORIZED_TOKEN);
		// Then
		권한이_없음을_검증(response);
	}

	/**
	 * Given 즐겨찾기를 생성하고
	 * When 즐겨찾기를 삭제하면
	 * Then UnAuthorized 예외 응답을 받는다.
	 */
	@DisplayName("비로그인 유저의 즐겨찾기 삭제")
	@Test
	void deleteFavorite_NO_LOGIN_USER() {
		// Given
		String token = 회원_생성과_베어러_인증_로그인_요청(EMAIL, PASSWORD, AGE);
		로그인_회원의_즐겨찾기_생성_요청(token, 강남역, 양재역);
		// When
		ExtractableResponse<Response> response = 로그인_회원의_즐겨찾기_제거_요청(UNAUTHORIZED_TOKEN, 1L);
		// Then
		권한이_없음을_검증(response);
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
}
