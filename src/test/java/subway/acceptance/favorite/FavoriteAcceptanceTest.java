package subway.acceptance.favorite;

import static org.assertj.core.api.Assertions.*;
import static subway.fixture.acceptance.FavoriteAcceptanceSteps.*;
import static subway.fixture.acceptance.LineAcceptanceSteps.*;
import static subway.fixture.acceptance.MemberAcceptanceSteps.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import subway.acceptance.AcceptanceTest;
import subway.dto.favorite.FavoriteRequest;
import subway.dto.favorite.FavoriteResponse;
import subway.dto.line.LineResponse;
import subway.fixture.favorite.FavoriteRequestFixture;
import subway.utils.enums.Location;

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

		FavoriteResponse response = RestAssured.given().log().all()
			.auth().oauth2(로그인())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(favoriteRequest)
			.when().post(Location.FAVORITES.path())
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value())
			.extract()
			.as(FavoriteResponse.class);

		assertThat(response.getSource().getId()).isEqualTo(출발역);
		assertThat(response.getTarget().getId()).isEqualTo(도착역);
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

	/**
	 * given 로그인된 회원인지 검사한다.
	 * when 파라미터로 넘어온 즐겨찾기를 삭제한다.
	 * then 204를 응답 받는다.
	 */
	@DisplayName("즐겨찾기를 삭제한다.")
	@Test
	void deleteFavorite() {
		FavoriteResponse response = 즐겨찾기_생성().as(FavoriteResponse.class);

		RestAssured.given().log().all()
			.auth().oauth2(로그인())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete(Location.FAVORITES.path(response.getId()).toUriString())
			.then().log().all()
			.statusCode(HttpStatus.NO_CONTENT.value());

		List<FavoriteResponse> 즐겨찾기_조회 = 즐겨찾기_조회();
		Assertions.assertThat(즐겨찾기_조회).hasSize(0);
	}

	private List<FavoriteResponse> 즐겨찾기_조회() {
		return RestAssured.given().log().all()
			.auth().oauth2(로그인())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get(Location.FAVORITES.path())
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract()
			.jsonPath()
			.getList("", FavoriteResponse.class);
	}
}
