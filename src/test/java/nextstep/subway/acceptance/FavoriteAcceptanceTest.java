package nextstep.subway.acceptance;

import static nextstep.subway.steps.FavoriteSteps.*;
import static nextstep.subway.steps.LineSteps.*;
import static nextstep.subway.steps.MemberSteps.*;
import static nextstep.subway.steps.StationSteps.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.steps.LineSteps;

@DisplayName("즐겨찾기 기능 테스트")
public class FavoriteAcceptanceTest extends AcceptanceTest {
	private final String A_EMAIL = "admin@email.com";
	private final String A_PASSWORD = "password";

	private final String EMAIL = "member@email.com";
	private final String PASSWORD = "password";

	private Long 교대역;
	private Long 강남역;
	private Long 양재역;
	private Long 남부터미널역;

	private Long 이호선;
	private Long 신분당선;
	private Long 삼호선;

	private String AdminMember;
	private String UserMember;

	/**
	 * 				(di:10, dr:2)
	 * 교대역		---	*2호선*	---	강남역
	 * |								|
	 * *3호선*						*신분당선*
	 * (di:2, dr:10)					(di:10, dr:3)
	 * |								|
	 * 남부터미널역	---	*3호선*	---	양재역
	 * 					(di:3, dr:10)
	 */
	@BeforeEach
	public void setUp() {
		super.setUp();

		AdminMember = 베어러_인증_로그인_요청(A_EMAIL, A_PASSWORD).jsonPath().getString("accessToken");
		UserMember = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

		교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
		강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
		양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
		남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

		이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10);
		신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10);
		삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2);

		지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3));
	}

	@DisplayName("즐겨찾기 생성")
	@Test
	void createFavoriteTest() {
		// when
		ExtractableResponse<Response> createResponse = createFavorite(UserMember, 교대역, 양재역);

		// then
		즐겨찾기_생성_성공(createResponse);
	}

	@DisplayName("즐겨찾기 조회")
	@Test
	void showFavoriteTest() {
		// when
		ExtractableResponse<Response> showResponse = showFavorite(UserMember);

		// then
		즐겨찾기_목록_성공(showResponse);
	}

	@DisplayName("즐겨찾기 삭제")
	@Test
	void deleteFavoriteTest() {
		// given
		ExtractableResponse<Response> createResponse = createFavorite(UserMember, 교대역, 양재역);

		// when
		ExtractableResponse<Response> deleteResponse = deleteFavorite(UserMember, createResponse);

		// then
		즐겨찾기_삭제_성공(deleteResponse);
	}

	@DisplayName("즐겨찾기 기능 사용 시 권한이 없는 경우")
	@ParameterizedTest
	@ValueSource(strings = {"", "TEST_TEST_TEST"})
	void unauthorizedFavoriteTest(String token) {
		// when
		ExtractableResponse<Response> showResponse = showFavorite(token);

		// then
		assertThat(showResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
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
