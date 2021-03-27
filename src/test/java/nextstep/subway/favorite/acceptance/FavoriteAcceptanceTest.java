package nextstep.subway.favorite.acceptance;

import static nextstep.subway.line.acceptance.LineSteps.*;
import static nextstep.subway.member.MemberSteps.*;
import static nextstep.subway.station.StationSteps.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기를 관리한다")
public class FavoriteAcceptanceTest extends AcceptanceTest {

	private static final String EMAIL = "email@email.com";
	private static final String PASSWORD = "password";
	private static final int AGE = 21;
	private StationResponse 교대역;
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 남부터미널역;
	private LineResponse 이호선;
	private LineResponse 신분당선;
	private LineResponse 삼호선;
	private String ACCESS_TOKEN;

	@BeforeEach
	public void setUp() {
		super.setUp();

		교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
		강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
		남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

		이호선 = 지하철_노선_등록되어_있음("2호선", "green", 교대역, 강남역, 10);
		신분당선 = 지하철_노선_등록되어_있음("신분당선", "green", 강남역, 양재역, 10);
		삼호선 = 지하철_노선_등록되어_있음("3호선", "green", 교대역, 남부터미널역, 2);

		지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 3);

		회원_생성_요청(EMAIL, PASSWORD, AGE);
		ACCESS_TOKEN = 로그인_되어_있음(EMAIL, PASSWORD).getAccessToken();
	}

	@DisplayName("즐겨찾기 생성을 요청한다")
	@Test
	void createFavorites() {
		// given
		Map<String, String> favoritesParams = new HashMap<>();
		favoritesParams.put("source", 교대역.getId() + "");
		favoritesParams.put("target", 양재역.getId() + "");

		// when
		ExtractableResponse<Response> response = 즐겨찾기_생성_요청(ACCESS_TOKEN, favoritesParams);

		// then
		즐겨찾기_생성됨(response);
	}

	@DisplayName("즐겨찾기 목록을 조회한다")
	@Test
	void getFavorites() {
		// given
		Map<String, String> favoritesParams = new HashMap<>();
		favoritesParams.put("source", 교대역.getId() + "");
		favoritesParams.put("target", 양재역.getId() + "");
		즐겨찾기_생성_요청(ACCESS_TOKEN, favoritesParams);

		// when
		ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(ACCESS_TOKEN);

		// then
		즐겨찾기_목록_조회됨(response);
	}

	@DisplayName("즐겨찾기 삭제를 요청한다")
	@Test
	void deleteFavorites() {

	}

	@DisplayName("즐겨찾기 정보를 관리한다")
	@Test
	void manageFavorites() {

	}

	private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).contains("/favorites/");
	}

	private ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Map<String, String> params) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(accessToken)
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/favorites")
			.then().log().all().extract();
	}

	private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(accessToken)
			.when().get("/favorites")
			.then().log().all().extract();
	}
}
