package nextstep.favorite.acceptance;

import static nextstep.auth.acceptance.AuthSteps.깃허브_로그인_요청;
import static nextstep.auth.acceptance.GithubUserFixture.사용자1;
import static nextstep.auth.acceptance.GithubUserFixture.사용자2;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_등록_요청;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_목록_조회_요청;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteResponse;
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

		accessToken = 깃허브_로그인_요청(사용자1.getCode()).jsonPath().getString("accessToken");
	}

	/**
	 * Given 역, 노선,구간을 등록하
	 * When 로그인한 상태에서 즐겨찾기를 등록하면
	 * Then 즐겨찾기 목록에서 생성된 즐겨찾기를 확인 할 수 있다.
	 */
	@Test
	void 즐겨찾기를_등록한다() {
		// when
		var response = 즐겨찾기_등록_요청(accessToken, 신사역.getId(), 논현역.getId());

		// then
		응답상태_코드_검증(response, HttpStatus.CREATED);
		즐겨찾기_목록_검증(신사역.getName(), 논현역.getName());
	}

	/**
	 * Given 역, 노선, 구간을 등록하고
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
	 * Given 역, 노선, 구간을 등록하고
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
	 * Given 역, 노선, 구간을 등록하고
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

	/**
	 * Given 역, 노선, 구간, 즐겨찾기를 등록하고
	 * When 로그인 상태에서 즐겨찾기 목록을 조회하면
	 * Then 즐겨찾기 목록을 확인 할 수 있다.
	 */
	@Test
	void 즐겨찾기_목록을_조회한다() {
		// given
		즐겨찾기_등록_요청(accessToken, 신사역.getId(), 논현역.getId());

		// when
		var response = 즐겨찾기_목록_조회_요청(accessToken);

		// then
		응답상태_코드_검증(response, HttpStatus.OK);
		즐겨찾기_목록_검증(신사역.getName(), 논현역.getName());
	}

	/**
	 * Given 역, 노선, 구간을 등록하고
	 * When 즐겨찾기를 등록 시 로그인 하지 않은 상태이면
	 * Then 요청이 실패된다.
	 */
	@Test
	void 즐겨찾기를_목록_조회_시_로그인_하지_않은_상태이면_요청이_실패된다() {
		// when
		String accessToken = "test";
		var response = 즐겨찾기_목록_조회_요청(accessToken);

		// then
		응답상태_코드_검증(response, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Given 역, 노선, 구간, 즐겨찾기를 등록하고
	 * When 로그인 상태에서 즐겨찾기 삭제하면
	 * Then 즐겨찾기 목록에서 삭제된 즐겨찾기를 찾을 수 없다.
	 */
	@Test
	void 즐겨찾기를_삭제한다() {
		// given
		Long 즐겨찾기_아이디 = 즐겨찾기_등록_후_아이디_가져오기(accessToken, 신사역.getId(), 논현역.getId());

		// when
		var response = 즐겨찾기_삭제_요청(accessToken, 즐겨찾기_아이디);

		// then
		응답상태_코드_검증(response, HttpStatus.NO_CONTENT);
		즐겨찾기_목록_검증();
	}

	/**
	 * Given 역, 노선, 구간, 즐겨찾기를 등록하고
	 * When 즐겨찾기 삭제 시 등록자가 아니면
	 * Then 요청이 실패된다.
	 */
	@Test
	void 즐겨찾기를_삭제_시_등록자가_아니면_요청이_실패된다() {
		// given
		Long 즐겨찾기_아이디 = 즐겨찾기_등록_후_아이디_가져오기(accessToken, 신사역.getId(), 논현역.getId());
		String accessToken = 깃허브_로그인_요청(사용자2.getCode()).jsonPath().getString("accessToken");

		// when
		var response = 즐겨찾기_삭제_요청(accessToken, 즐겨찾기_아이디);

		// then
		응답상태_코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Given 역, 노선, 구간, 즐겨찾기를 등록하고
	 * When 즐겨찾기 삭제 시 로그인 하지 않은 상태이면
	 * Then 요청이 실패된다.
	 */
	@Test
	void 즐겨찾기를_삭제_시_로그인_하지_않은_상태이면_요청이_실패된다() {
		// given
		Long 즐겨찾기_아이디 = 즐겨찾기_등록_후_아이디_가져오기(accessToken, 신사역.getId(), 논현역.getId());
		String accessToken = "test";

		// when
		var response = 즐겨찾기_삭제_요청(accessToken, 즐겨찾기_아이디);

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
		assertThat(response.statusCode()).isEqualTo(httpStatus.value());
	}

	private void 즐겨찾기_목록_검증(String... expectedStationNames) {
		List<FavoriteResponse> favoriteResponses = 즐겨찾기_목록_조회_요청(accessToken).jsonPath()
			.getList("", FavoriteResponse.class);

		if (favoriteResponses.isEmpty() && expectedStationNames.length == 0) {
			assertThat(favoriteResponses).isEmpty();
			return;
		}

		String stationNames = favoriteResponses.stream()
			.map(f -> f.getSource().getName())
			.collect(Collectors.joining(", "));
		stationNames = String.format("[%s, %s]", stationNames,
			favoriteResponses.get(favoriteResponses.size() - 1).getTarget().getName());

		assertThat(stationNames).isEqualTo(Arrays.asList(expectedStationNames).toString());
	}

	private Long 즐겨찾기_등록_후_아이디_가져오기(String accessToken, Long source, Long target) {
		String location = 즐겨찾기_등록_요청(accessToken, source, target).header("Location");
		return Long.valueOf(location.split("/")[2]);
	}
}
