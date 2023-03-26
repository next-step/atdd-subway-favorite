package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class FavoriteSteps {
	public static ExtractableResponse<Response> 로그인_회원의_즐겨찾기_생성_요청(String token, Long source, Long target) {
		Map<String, String> params = new HashMap<>();
		params.put("source", source + "");
		params.put("target", target + "");
		return RestAssured.given().log().all()
			.header("authorization", "Bearer " + token)
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.post("/favorites")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 로그인_회원의_즐겨찾기_목록_조회_요청(String token) {
		return RestAssured.given().log().all()
			.header("authorization", "Bearer " + token)
			.when().get("/favorites")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 로그인_회원의_즐겨찾기_제거_요청(String token, Long favoriteId) {
		return RestAssured.given().log().all()
			.header("authorization", "Bearer " + token)
			.when().delete("/favorites/" + favoriteId)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 비로그인_회원의_즐겨찾기_생성_요청(Long source, Long target) {
		Map<String, String> params = new HashMap<>();
		params.put("source", source + "");
		params.put("target", target + "");
		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.post("/favorites")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 비로그인_회원의_즐겨찾기_목록_조회_요청() {
		return RestAssured.given().log().all()
			.when().get("/favorites")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 비로그인_회원의_즐겨찾기_제거_요청(Long favoriteId) {
		return RestAssured.given().log().all()
			.when().delete("/favorites/", favoriteId)
			.then().log().all()
			.extract();
	}

	public static void 즐겨찾기_생성_검증(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

	}

	public static void 즐겨찾기_삭제_검증(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	public static void 즐겨찾기_목록_조회_검증(ExtractableResponse<Response> response, List<Long> sources, List<Long> targets) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getList("source.id", Long.class)).containsOnly(sources.toArray(new Long[0]));
		assertThat(response.jsonPath().getList("target.id", Long.class)).containsOnly(targets.toArray(new Long[0]));
	}
}
