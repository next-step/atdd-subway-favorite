package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps extends AcceptanceTestSteps{
	public static ExtractableResponse<Response> 로그인_상태에서_즐겨찾기에_추가한다(String member, long 광교역, long 광교중앙역) {
		Map<String, Object> params = new HashMap<>();
		params.put("source", 광교역);
		params.put("target", 광교중앙역);

		return given(member)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(params)
				.when()
				.post("/favorite")
				.then()
				.log().all()
				.extract();
	}

	public static ExtractableResponse<Response> 로그인_안_한_상태로_즐겨찾기에_추가한다(long 광교역, long 광교중앙역) {
		Map<String, Object> params = new HashMap<>();
		params.put("source", 광교역);
		params.put("target", 광교중앙역);

		return given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(params)
				.when()
				.post("/favorite")
				.then()
				.log().all()
				.extract();
	}
}
