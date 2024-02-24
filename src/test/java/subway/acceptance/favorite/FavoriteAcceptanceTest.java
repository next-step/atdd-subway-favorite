package subway.acceptance.favorite;

import static subway.fixture.acceptance.LineAcceptanceSteps.*;
import static subway.fixture.acceptance.MemberAcceptanceSteps.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import subway.acceptance.AcceptanceTest;
import subway.dto.favorite.FavoriteRequest;
import subway.dto.line.LineResponse;
import subway.fixture.favorite.FavoriteRequestFixture;

@DisplayName("즐겨찾기 인수 테스트")
class FavoriteAcceptanceTest extends AcceptanceTest {
	/**
	 * given 로그인된 회원인지 검사한다.
	 * when 즐겨찾기를 생성한다.
	 * then 생성된 즐겨찾기를 응답받는다.
	 */
	@DisplayName("즐겨찾기를 생성한다.")
	@Test
	void saveFavorite() {
		멤버_생성();
		LineResponse 노선_생성 = 노선_생성();
		Long 출발역 = 노선_생성.getStations().get(0).getId();
		Long 도착역 = 노선_생성.getStations().get(1).getId();

		FavoriteRequest favoriteRequest = FavoriteRequestFixture.builder()
			.source(출발역)
			.target(도착역)
			.build();

		RestAssured.given().log().all()
			.auth().oauth2(로그인())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(favoriteRequest)
			.when().post("/favorites")
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value());
	}

	/**
	 * given 로그인된 회원인지 검사한다.
	 * when 모든 즐겨찾기를 조회한다.
	 * then 조회된 즐겨찾기를 응답받는다.
	 */
	@DisplayName("즐겨찾기를 조회한다.")
	@Test
	void findFavorite() {
		FavoriteResponse response = 즐겨찾기_생성().as(FavoriteResponse.class);

		List<FavoriteResponse> responses = 즐겨찾기_조회();

		assertThat(List.of(response)).usingRecursiveComparison().isEqualTo(responses);
	}

}
