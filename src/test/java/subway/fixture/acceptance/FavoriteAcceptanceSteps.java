package subway.fixture.acceptance;

import static subway.fixture.acceptance.LineAcceptanceSteps.*;
import static subway.fixture.acceptance.MemberAcceptanceSteps.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.favorite.FavoriteRequest;
import subway.dto.line.LineResponse;
import subway.fixture.favorite.FavoriteRequestFixture;
import subway.utils.enums.Location;

public class FavoriteAcceptanceSteps {
	public static ExtractableResponse<Response> 즐겨찾기_생성() {
		멤버_생성();
		LineResponse 노선_생성 = 노선_생성();
		Long 출발역 = 노선_생성.getStations().get(0).getId();
		Long 도착역 = 노선_생성.getStations().get(1).getId();

		FavoriteRequest favoriteRequest = FavoriteRequestFixture.builder()
			.source(출발역)
			.target(도착역)
			.build();

		return RestAssured.given().log().all()
			.auth().oauth2(로그인())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(favoriteRequest)
			.when().post(Location.FAVORITES.path())
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value())
			.extract();
	}
}
