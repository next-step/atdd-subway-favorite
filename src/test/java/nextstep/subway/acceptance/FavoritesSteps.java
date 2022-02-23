package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoritesSteps {

	public static ExtractableResponse<Response> 즐겨찾기_생성_요청(final String accessToken, final Long source, final Long target) {

		Map<String, String> favorites;
		favorites = new HashMap<>();
		favorites.put("source", source + "");
		favorites.put("target", target + "");

		return RestAssured.given().log().all()
						.auth().oauth2(accessToken)
						.body(favorites)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when().post("/favorites")
						.then().log().all()
						.statusCode(HttpStatus.CREATED.value())
						.extract();
	}

	public static ExtractableResponse<Response> 즐겨찾기_목록_조회(final String accessToken) {

		return RestAssured.given().log().all()
						.auth().oauth2(accessToken)
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.when().get("/favorites")
						.then().log().all()
						.statusCode(HttpStatus.OK.value())
						.extract();
	}
}
