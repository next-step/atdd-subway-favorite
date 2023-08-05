package nextstep.favorite.acceptance;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteRequest;

public class FavoriteSteps {

	public static ExtractableResponse<Response> 즐겨찾기_등록_요청(String accessToken, Long source, Long target) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new FavoriteRequest(source, target))
			.when().post("/favorites")
			.then().log().all().extract();
	}
}
