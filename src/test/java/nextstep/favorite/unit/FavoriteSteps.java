package nextstep.favorite.unit;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {
	public static ExtractableResponse<Response> 즐겨찾기_생성_요청(Long source, Long target) {
		Map<String, String> params = new HashMap<>();
		params.put("source", String.valueOf(source));
		params.put("target", String.valueOf(target));

		return RestAssured.given().log().all()
				.body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/favorites")
				.then().log().all()
				.extract();
	}

	public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
		return RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.get("/favorites")
				.then().log().all()
				.extract();
	}

	public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(Long id) {
		return RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.delete("/favorites/{id}", id)
				.then().log().all()
				.extract();
	}
}
