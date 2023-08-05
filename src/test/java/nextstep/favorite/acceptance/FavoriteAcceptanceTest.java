package nextstep.favorite.acceptance;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_등록_요청;
import static nextstep.auth.acceptance.AuthSteps.깃허브_로그인_요청;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.acceptance.GithubUserFixture;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.utils.AcceptanceTest;

public class FavoriteAcceptanceTest extends AcceptanceTest {

	private String accessToken;
	private StationResponse 신사역;
	private StationResponse 논현역;
	private StationResponse 신논현역;


	@BeforeEach
	void init() {
		신사역 = 지하철역_생성_요청("신사역").as(StationResponse.class);
		논현역 = 지하철역_생성_요청("논현역").as(StationResponse.class);
		신논현역 = 지하철역_생성_요청("신논현역").as(StationResponse.class);

		long lineId = 지하철_노선_생성_요청("신분당선", "Red").jsonPath().getLong("id");
		지하철_노선에_지하철_구간_생성_요청(lineId, createSectionCreateParams(신사역.getId(), 논현역.getId()));

		accessToken = 깃허브_로그인_요청(GithubUserFixture.사용자1.getCode()).jsonPath().getString("accessToken");
	}

	/**
	 * Given 역과 노선과 구간을 등록하고
	 * When 로그인한 상태에서 즐겨찾기를 등록하면
	 * Then 즐겨찾기 전체 목록에서 생성된 즐겨찾기를 확인 할 수 있다.
	 */
	@Test
	void 즐겨찾기를_등록한다() {
		// when
		var response = 즐겨찾기_등록_요청(accessToken, 신사역.getId(), 논현역.getId());

		// then
		응답상태_코드_검증(response, HttpStatus.CREATED);
	}

	/**
	 * Given 역과 노선과 구간을 등록하고
	 * When 로그인한 상태에서 즐겨찾기를 등록 시 비정상 경로를 입력하면
	 * Then 요청이 실패된다.
	 */
	@Test
	void 즐겨찾기를_등록_시_입력하면_요청이_실패된다() {
		// when

		var response = 즐겨찾기_등록_요청(accessToken, 논현역.getId(), 신논현역.getId());

		// then
		응답상태_코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Given 역과 노선과 구간을 등록하고
	 * When 로그인한 상태에서 즐겨찾기를 등록 시 등록되지 않은 역을 입력하면
	 * Then 요청이 실패된다.
	 */
	@Test
	void 즐겨찾기를_등록_시_등록되지_않은_역을_입력하면_요청이_실패된다() {
		// when
		Long 등록되지_않은_역_아아디 = 10L;
		var response = 즐겨찾기_등록_요청(accessToken, 논현역.getId(), 등록되지_않은_역_아아디);

		// then
		응답상태_코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Given 역과 노선과 구간을 등록하고
	 * When 즐겨찾기를 등록 시 로그인 하지 않은 상태이면
	 * Then 요청이 실패된다.
	 */
	@Test
	void 즐겨찾기를_등록_시_로그인_하지_않은_상태이면_요청이_실패된다() {
		// when
		String accessToken = "test";
		var response = 즐겨찾기_등록_요청(accessToken, 논현역.getId(), 신논현역.getId());

		// then
		응답상태_코드_검증(response, HttpStatus.UNAUTHORIZED);
	}

	private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId) {
		Map<String, String> params = new HashMap<>();
		params.put("upStationId", String.valueOf(upStationId));
		params.put("downStationId", String.valueOf(downStationId));
		params.put("distance", "10");
		return params;
	}

	private void 응답상태_코드_검증(ExtractableResponse<Response> response, HttpStatus httpStatus) {
		Assertions.assertThat(response.statusCode()).isEqualTo(httpStatus.value());
	}
}
