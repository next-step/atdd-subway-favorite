package subway.acceptance.favorite;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.acceptance.AcceptanceTest;
import subway.dto.favorite.FavoriteRequest;
import subway.member.Member;
import subway.member.MemberRepository;

@DisplayName("즐겨찾기 인수 테스트")
class FavoriteAcceptanceTest extends AcceptanceTest {
	public static final String EMAIL = "admin@email.com";
	public static final String PASSWORD = "password";
	public static final Integer AGE = 20;

	@Autowired
	private MemberRepository memberRepository;

	/**
	 * given 로그인된 회원인지 검사한다.
	 * when 즐겨찾기를 생성한다.
	 * then 생성된 즐겨찾기를 응답받는다.
	 */
	@DisplayName("즐겨찾기를 생성한다.")
	@Test
	void saveFavorite() {
		FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 3L);
		memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

		Map<String, String> params = new HashMap<>();
		params.put("email", EMAIL);
		params.put("password", PASSWORD);

		ExtractableResponse<Response> response = RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when().post("/login/token")
			.then().log().all()
			.statusCode(HttpStatus.OK.value()).extract();

		String accessToken = response.jsonPath().getString("accessToken");
		assertThat(accessToken).isNotBlank();

		ExtractableResponse<Response> extract = RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(favoriteRequest)
			.when().post("/favorites")
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value())
			.extract();
	}
}
