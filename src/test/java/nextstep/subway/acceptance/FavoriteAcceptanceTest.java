package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.LineSectionAcceptanceTest.*;
import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class FavoriteAcceptanceTest extends AcceptanceTest {

	long 강남역;
	long 양재역;
	private String adminAccessToken;
	private String memberAccessToken;

	@BeforeEach
	public void setUp() {
		super.setUp();
		adminAccessToken = 로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD);
		memberAccessToken = 로그인_되어_있음(MEMBER_EMAIL, MEMBER_PASSWORD);
		강남역 = 지하철역_생성_요청("강남역", adminAccessToken).jsonPath().getLong("id");
		양재역 = 지하철역_생성_요청("양재역", adminAccessToken).jsonPath().getLong("id");

		Map<String, String> lineCreateParams = createLineCreateParams(강남역, 양재역);
		지하철_노선_생성_요청(lineCreateParams, adminAccessToken).jsonPath().getLong("id");
	}

	@Test
	@DisplayName("즐겨찾기_등록_권한있는_경우")
	void registerFavoriteByMember() {
		//given
		ExtractableResponse<Response> response = 즐겨찾기_등록(강남역, 양재역, adminAccessToken);

		//when
		ExtractableResponse<Response> 즐겨찾기조회 = 즐겨찾기_조회(response);

		//then
		Assertions.assertThat(즐겨찾기조회.jsonPath().getList("source.id", Long.class))
			.hasSize(2)
			.containsExactly(강남역, 양재역);
	}

	@Test
	@DisplayName("즐겨찾기_등록_권한없는_경우")
	void registerFavoriteByGuest() {
		//when
		ExtractableResponse<Response> response = 즐겨찾기_등록(강남역, 양재역, memberAccessToken);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	@Test
	@DisplayName("즐겨찾기_삭제_권한있는_경우")
	void removeFavoriteByMember() {
		//given
		ExtractableResponse<Response> response = 즐겨찾기_등록(강남역, 양재역, adminAccessToken);
		즐겨찾기_삭제(response, adminAccessToken);

		//when
		ExtractableResponse<Response> 즐겨찾기조회 = 즐겨찾기_조회(response);

		//then
		assertThat(즐겨찾기조회.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(즐겨찾기조회).isNull();
	}

	@Test
	@DisplayName("즐겨찾기_삭제_권한없는_경우")
	void removeFavoriteByGuest() {
		//given
		ExtractableResponse<Response> response = 즐겨찾기_등록(강남역, 양재역, adminAccessToken);

		//when
		즐겨찾기_삭제(response, memberAccessToken);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
}
