package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.steps.LineSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static nextstep.auth.utils.steps.AuthSteps.토큰_생성_요청;
import static nextstep.member.utils.steps.MemberSteps.회원_생성_요청;
import static nextstep.subway.utils.steps.FavoriteSteps.*;
import static nextstep.subway.utils.steps.StationSteps.역_삭제_요청;
import static nextstep.subway.utils.steps.StationSteps.역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FavoriteAcceptanceTest {
	private Long 종로3가역;
	private Long 시청역;
	private Long 서울역;
	private String accessToken;

	@BeforeEach
	public void setup() {
		종로3가역 = 역_생성_요청("종로3가역").jsonPath().getLong("id");
		시청역 = 역_생성_요청("시청역").jsonPath().getLong("id");
		서울역 = 역_생성_요청("서울역").jsonPath().getLong("id");

		LineSteps.노선_생성_요청("분당선", "노랑", 종로3가역, 시청역, 6);

		String email = "aab555586@gmail.com";
		String password = "password";
		int age = 20;

		회원_생성_요청(email, password, age);
		accessToken = 토큰_생성_요청(email, password).jsonPath().getString("accessToken");
	}

	/**
	 * Scenario: 즐겨찾기 생성 성공
	 * Given 상행역 : 종로3가역 / 하행역 : 시청역 / 길이 : 6 인 구간이 존재하는 노선을 생성한다.
	 * When 종로3가역부터 시청역까지의 경로를 즐겨찾기로 생성한다.
	 * Then 즐겨찾기 리스트 조회 시, 생성한 즐겨찾기가 조회된다.
	 */
	@DisplayName("즐겨찾기 생성을 성공한다.")
	@Test
	void 즐겨찾기_생성() {
		// when
		ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 종로3가역, 시청역);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		assertThat(즐겨찾기_전체_조회_요청(accessToken).jsonPath().getList("id", Long.class)).contains(get생성한_즐겨찾기_ID(response));
	}

	/**
	 * Scenario: 존재하지 않는 역이 포함된 경로를 즐겨찾기로 생성할 경우 생성 실패
	 * Given 상행역 : 종로3가역 / 하행역 : 시청역 / 길이 : 6 인 구간들이 존재하는 노선을 생성한다.
	 * When 종로3울역부터 서울역까지의 경로를 즐겨찾기로 생성한다.
	 * Then "역이 존재하지 않습니다."라는 메시지를 반환한다.
	 */
	@DisplayName("존재하지 않는 경로를 즐겨찾기로 생성할 시, 즐겨찾기 생성이 실패한다.")
	@Test
	void 존재하지_않는_역_즐겨찾기_생성_실패() {
		// when
		역_삭제_요청(서울역);
		ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 종로3가역, 서울역);

		// then
		실패시_코드값_메시지_검증(response, HttpStatus.BAD_REQUEST.value(), "역이 존재하지 않습니다.");
	}


		/**
	 * Scenario: 존재하지 않는 경로를 즐겨찾기로 생성할 경우 생성 실패
	 * ````
	 * Given 상행역 : 종로3가역 / 하행역 : 시청역 / 길이 : 6 인 구간들이 존재하는 노선을 생성한다.
	 * Given 상행역 : 동대문역 / 하행역 : 서울역 / 길이 : 20 인 구간들이 존재하는 노선을 생성한다.
	 * When 종로3가역부터 동대문역까지의 경로를 즐겨찾기로 생성한다.
	 * Then "경로가 존재하지 않습니다."라는 메시지를 반환한다.
	 */
	@DisplayName("존재하지 않는 경로를 즐겨찾기로 생성할 시, 즐겨찾기 생성이 실패한다.")
	@Test
	void 존재하지_않는_경로_즐겨찾기_생성_실패() {
		// given
		Long 동대문역 = 역_생성_요청("동대문역").jsonPath().getLong("id");

		LineSteps.노선_생성_요청("4호선", "하늘", 동대문역, 서울역, 6);

		// when
		ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 종로3가역, 동대문역);

		// then
		실패시_코드값_메시지_검증(response, HttpStatus.BAD_REQUEST.value(), "경로가 존재하지 않습니다.");
	}

	/**
	 * Scenario: 즐겨찾기 조회 성공
	 * Given 상행역 : 종로3가역 / 하행역 : 시청역 / 길이 : 6 인 구간이 존재하는 노선을 생성한다.
	 * Given 종로3가역부터 시청역까지의 경로를 즐겨찾기로 생성한다.
	 * When 즐겨찾기 리스트를 조회하면
	 * Then 생성한 즐겨찾기를 목록에서 조회할 수 있다.
	 */
	@DisplayName("즐겨찾기 조회를 성공한다.")
	@Test
	void 즐겨찾기_조회_성공() {
		// given
		즐겨찾기_생성_요청(accessToken, 종로3가역, 시청역);

		// when
		ExtractableResponse<Response> response = 즐겨찾기_전체_조회_요청(accessToken);

		//
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getList("source.id", Long.class)).contains(종로3가역);
		assertThat(response.jsonPath().getList("target.id", Long.class)).contains(시청역);
	}

	/**
	 * Given 상행역 : 종로3가역 / 하행역 : 시청역 / 길이 : 6 인 구간이 존재하는 노선을 생성한다.
	 * Given 종로3가역부터 시청역까지의 경로를 즐겨찾기로 생성한다.
	 * When 생성한 즐겨찾기를 삭제하면
	 * Then 즐겨찾기 목록 조회 시 해당 즐겨찾기가 존재하지 앟는다.
	 */
	@DisplayName("즐겨찾기 삭제를 성공한다.")
	@Test
	void 즐겨찾기_삭제_성공() {
		// given
		Long id = get생성한_즐겨찾기_ID(즐겨찾기_생성_요청(accessToken, 종로3가역, 시청역));

		// when
		ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(accessToken, id);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
		assertThat(즐겨찾기_전체_조회_요청(accessToken).jsonPath().getList("id", Long.class)).doesNotContain(id);
	}

	/**
	 * When 존재하지 않는 즐겨찾기를 삭제하면
	 * Then "즐겨찾기가 존재하지 않습니다."라는 메시지를 반환한다.
	 */
	@DisplayName("존재하지 않는 즐겨찾기를 삭제할 시, 즐겨찾기 삭제가 실패한다.")
	@Test
	void 존재하지_않는_즐겨찾기_삭제_실패() {
		// given
		Long id = get생성한_즐겨찾기_ID(즐겨찾기_생성_요청(accessToken, 종로3가역, 시청역));

		// when
		즐겨찾기_삭제_요청(accessToken, id);
		ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(accessToken, id);

		// then
		실패시_코드값_메시지_검증(response, HttpStatus.BAD_REQUEST.value(), "즐겨찾기가 존재하지 않습니다.");
	}

	/**
	 * Scenario: 인증정보 없이 즐겨찾기를 생성, 조회, 삭제 시 실패한다.
	 * When 인증정보 없이 즐겨찾기를 생성, 조회, 삭제 할 경우
	 * Then "인증정보가 존재하지 않습니다."라는 메시지를 반환한다.
	 */
	@DisplayName("인증정보 없이 즐겨찾기 생성, 조회, 삭제 시도 시, 즐겨찾기 생성이 실패한다.")
	@Test
	void 인증정보_없이_즐겨찾기_생성_실패() {
		// when & then
		실패시_코드값_메시지_검증(즐겨찾기_생성_요청("", 종로3가역, 서울역), HttpStatus.UNAUTHORIZED.value(), "인증정보가 존재하지 않습니다.");
		실패시_코드값_메시지_검증(즐겨찾기_전체_조회_요청(""), HttpStatus.UNAUTHORIZED.value(), "인증정보가 존재하지 않습니다.");
		실패시_코드값_메시지_검증(즐겨찾기_삭제_요청("",1L), HttpStatus.UNAUTHORIZED.value(), "인증정보가 존재하지 않습니다.");
	}

	private Long get생성한_즐겨찾기_ID(ExtractableResponse<Response> response) {
		return Long.valueOf(response.header("Location").replace("/favorites/", ""));
	}

	private void 실패시_코드값_메시지_검증(ExtractableResponse<Response> response, int statusCode, String message) {
		assertThat(response.statusCode()).isEqualTo(statusCode);
		assertThat(response.body().jsonPath().getString("message")).isEqualTo(message);
	}
}
