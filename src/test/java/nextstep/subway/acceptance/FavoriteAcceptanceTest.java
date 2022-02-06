package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.StationSteps.아이디_추출;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

@DisplayName("지하철 경로 즐겨찾기 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

	final String EMAIL = "email@email.com";
	final String PASSWORD = "password";
	long 가양역;
	long 강남역;
	String JWT_토큰;

	/**
	 * Given 지하철역 등록되어 있음
	 * And 지하철 노선 등록되어 있음
	 * And 지하철 노선에 지하철역 등록되어 있음
	 * And 회원 등록되어 있음
	 * And 로그인 되어있음
	 */
	@BeforeEach
	public void setUp() {
		super.setUp();
		// 지하철 역
		가양역 = 아이디_추출(지하철역_생성_요청("가양역"));
		강남역 = 아이디_추출(지하철역_생성_요청("강남역"));

		// 지하철 노선
		long 구호선 = 아이디_추출(지하철_노선_생성_요청("9호선", "yellow"));
		지하철_노선에_지하철_구간_생성_요청(구호선, 지하철_구간_생성_파라미터(가양역, 강남역, 10));

		// 회원
		회원_생성_요청(EMAIL, PASSWORD, 10);
		JWT_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);
	}

	/**
	 * When 즐겨찾기 생성을 요청
	 * Then 즐겨찾기 생성됨
	 */
	@DisplayName("즐겨찾기를 생성한다.")
	@Test
	void addFavorite() {
		// when
		ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(JWT_토큰, 가양역, 강남역);

		// then
		FavoriteSteps.즐겨찾기_생성됨(즐겨찾기_생성_응답);
	}

	/**
	 * Given 즐겨찾기 생성됨
	 * When 즐겨찾기 목록 조회 요청
	 * Then 즐겨찾기 목록 조회됨
	 */
	@DisplayName("즐겨찾기를 조회한다.")
	@Test
	void getFavorite() {
		// given
		ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(JWT_토큰, 가양역, 강남역);

		// when
		ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(JWT_토큰);

		// then
		즐겨찾기_조회됨(즐겨찾기_조회_응답, 가양역, 강남역);
	}

	/**
	 * Given 즐겨찾기 생성되어 있음
	 * When 즐겨찾기 삭제 요청
	 * Then 즐겨찾기 삭제됨
	 */
	@DisplayName("즐겨찾기를 삭제한다.")
	@Test
	void removeFavorite() {
		// given
		ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(JWT_토큰, 가양역, 강남역);

		// when
		ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(JWT_토큰, 즐겨찾기_생성_응답);

		// then
		즐겨찾기_삭제됨(즐겨찾기_삭제_응답);
	}

	/**
	 * When 즐겨찾기 생성을 요청
	 * Then 즐겨찾기 생성됨
	 * When 즐겨찾기 목록 조회 요청
	 * Then 즐겨찾기 목록 조회됨
	 * When 즐겨찾기 삭제 요청
	 * Then 즐겨찾기 삭제됨
	 */
	@DisplayName("즐겨찾기를 관리한다.")
	@Test
	void manageFavorite() {
		// 생성
		ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(JWT_토큰, 가양역, 강남역);
		FavoriteSteps.즐겨찾기_생성됨(즐겨찾기_생성_응답);

		// 목록 조회
		ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(JWT_토큰);
		즐겨찾기_조회됨(즐겨찾기_조회_응답, 가양역, 강남역);

		// 삭제
		ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(JWT_토큰, 즐겨찾기_생성_응답);
		즐겨찾기_삭제됨(즐겨찾기_삭제_응답);
	}

}
