package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.authentication.AuthenticationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.FavoritesSteps.*;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기")
public class FavoritesAcceptanceTest extends AcceptanceTest {

	private static final String EMAIL = "email@email.com";
	private static final String PASSWORD = "password";
	private static final Integer AGE = 20;

	/**
	 *     Given 지하철역 등록되어 있음
	 *     And 지하철 노선 등록되어 있음
	 *     And 지하철 노선에 지하철역 등록되어 있음
	 *     And 회원 등록되어 있음
	 *     And 로그인 되어있음
	 */

	private Long 강남역;
	private Long 양재역;
	private Long 신분당선;
	private String accessToken;

	@BeforeEach
	private void init() {
		super.setUp();
		강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
		양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
		Map<String, String> lineCreateParams = createLineCreateParams(강남역, 양재역);
		신분당선 = 지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
		회원_생성_요청(EMAIL, PASSWORD, AGE);
		accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
	}

	/**
	 *     When 즐겨찾기 생성을 요청
	 *     Then 즐겨찾기 생성됨
	 */
	@DisplayName("즐겨찾기를 생성하다.")
	@Test
	void createFavorites() {
		// when
		ExtractableResponse<Response> result = 즐겨찾기_생성_요청(accessToken, 강남역, 양재역);

		// then
		assertThat(result.response().statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(result.header("Location")).isNotBlank();
	}

	/**
	 *  	 given 즐겨찾기 생성
	 *     When 즐겨찾기 목록 조회 요청
	 *     Then 즐겨찾기 목록 조회됨
	 */
	@DisplayName("즐겨찾기를 조회하다.")
	@Test
	void showFavorites() {
		// given
		즐겨찾기_생성_요청(accessToken, 강남역, 양재역);

		// when
		ExtractableResponse<Response> result = 즐겨찾기_목록_조회(accessToken);

		// then
		assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(result.jsonPath().getList("id", Long.class)).hasSize(1).containsExactly(1L);
	}

	/**
	 * 		given 즐겨찾기 생성
	 *    When 즐겨찾기 삭제 요청
	 *    Then 즐겨찾기 삭제됨
	 */
	@DisplayName("즐가찾기를 삭제하다")
	@Test
	void removeFavorites() {
		// given
		즐겨찾기_생성_요청(accessToken, 강남역, 양재역);

		// when
		ExtractableResponse<Response> result = 즐겨찾기_삭제(accessToken, 1L);

		// then
		assertThat(result.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	/**
	 * 비로그인 경우 401 Unauthorized 응답
	 */
	@DisplayName("비로그인 즐겨찾기 기능을 사용할 수 없다.")
	@Test
	void isUnauthorizedByLogin() {
		// given
		즐겨찾기_생성_요청(accessToken, 강남역, 양재역);
		accessToken = "";

		// when
		ExtractableResponse<Response> result = 즐겨찾기_목록_조회(accessToken);

		// then
		assertThat(result.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
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
