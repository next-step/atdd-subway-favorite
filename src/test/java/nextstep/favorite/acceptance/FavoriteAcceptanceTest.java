package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.favorite.unit.FavoriteSteps.*;
import static nextstep.member.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철노선_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관리 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
	private Long 이호선;

	private Long 홍대입구역;
	private Long 합정역;
	private Long 당산역;

	private String 로그인토큰;

	/**
	 * Given 지하철 역 등록되어 있음
	 */
	@Override
	@BeforeEach
	public void setUp() {
		super.setUp();

		// 지하철 역 등록되어 있음
		홍대입구역 = 지하철역_생성_요청("홍대입구역").jsonPath().getLong("id");
		합정역 = 지하철역_생성_요청("합정역").jsonPath().getLong("id");
		당산역 = 지하철역_생성_요청("당산역").jsonPath().getLong("id");

		// 지하철 노선 등록되어 있음
		이호선 = 지하철노선_생성_요청("2호선", "green", 홍대입구역, 당산역, 50).jsonPath().getLong("id");

		// 회원 등록되어 있음
		회원_생성_요청("email@email.com", "password", 30);

		// 로그인 되어 있음
		로그인_요청("email@email.com", "password");
		로그인토큰 = 로그인_되어_있음("email@email.com", "password");
	}

	/**
	 * When 즐겨찾기 생성을 요청
	 * Then 즐겨찾기 생성됨
	 */
	@DisplayName("즐겨찾기 생성")
	@Test
	void addFavorite() {
		// when
		ExtractableResponse<Response> response = 즐겨찾기_생성_요청(로그인토큰, 홍대입구역, 당산역);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isNotBlank();
	}

	/**
	 * When 즐겨찾기 목록 조회를 요청
	 * Then 즐겨찾기 목록 조회됨
	 */
	@DisplayName("즐겨찾기 목록 조회")
	@Test
	void searchFavorite() {
		// when
		즐겨찾기_생성_요청(로그인토큰, 홍대입구역, 당산역);
		즐겨찾기_생성_요청(로그인토큰, 합정역, 당산역);
		ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(로그인토큰);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		//왜 Long으로 던지고있는데 Integer로 반응이 올까? ㅠㅠ
		List<Integer> sourceIds = response.jsonPath().getList("source.id");
		assertThat(sourceIds).contains(1, 2);
	}

	/**
	 * When 즐겨찾기 삭제를 요청
	 * Then 즐겨찾기 삭제됨
	 */
	@DisplayName("즐겨찾기 삭제")
	@Test
	void removeFavorite() {
		// when
		String url = 즐겨찾기_생성_요청(로그인토큰, 홍대입구역, 당산역).header("Location");
		ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(로그인토큰, url);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	@DisplayName("비로그인 상태에서 즐겨찾기 서비스 접근")
	@Test
	void accessMyInfoInNotLogin() {
		String 유효하지_않은_토큰 = "invalid token";

		ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(유효하지_않은_토큰, 홍대입구역, 당산역);
		assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

		ExtractableResponse<Response> searchResponse = 즐겨찾기_목록_조회_요청(유효하지_않은_토큰);
		assertThat(searchResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

		ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(유효하지_않은_토큰, "/favorite/1");
		assertThat(searchResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
}
