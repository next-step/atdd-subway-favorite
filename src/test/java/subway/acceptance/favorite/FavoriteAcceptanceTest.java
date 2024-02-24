package subway.acceptance.favorite;

import static subway.fixture.acceptance.MemberAcceptanceSteps.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.acceptance.AcceptanceTest;
import subway.dto.favorite.FavoriteRequest;
import subway.dto.station.StationResponse;
import subway.fixture.acceptance.StationAcceptanceSteps;

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
		Long 불광역 = StationAcceptanceSteps.정류장_생성("불광역").as(StationResponse.class).getId();
		Long 양재역 = StationAcceptanceSteps.정류장_생성("양재역").as(StationResponse.class).getId();

		FavoriteRequest favoriteRequest = new FavoriteRequest(불광역, 양재역);

		ExtractableResponse<Response> extract = RestAssured.given().log().all()
			.auth().oauth2(로그인())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(favoriteRequest)
			.when().post("/favorites")
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value())
			.extract();
	}
}
