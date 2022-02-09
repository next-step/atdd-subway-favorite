package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class FavoriteSteps {

	private static final String LOCATION_HEADER = "Location";

	public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, long source, long target) {
		return RestAssured
				.given().log().all()
				.auth().oauth2(accessToken)
				.body(createParameters(source, target))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/favorites")
				.then().log().all().extract();
	}

	public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.header(LOCATION_HEADER)).isNotEmpty();
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> response) {
		return RestAssured
				.given().log().all()
				.auth().oauth2(accessToken)
				.when().delete(response.header(LOCATION_HEADER))
				.then().log().all().extract();
	}

	public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	public static void 즐겨찾기_조회됨(ExtractableResponse<Response> response, long sourceId, long targetId) {
		Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		Assertions.assertThat(response.jsonPath().getLong("source[0].id")).isEqualTo(sourceId);
		Assertions.assertThat(response.jsonPath().getLong("target[0].id")).isEqualTo(targetId);
	}

	public static Map<String, String> createParameters(long source, long target) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("source", source + "");
		parameters.put("target", target + "");

		return parameters;
	}

	public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
		return RestAssured
				.given().log().all()
				.auth().oauth2(accessToken)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when().get("/favorites")
				.then().log().all().extract();
	}
}
