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
	long 삼성역;
	private String member1AccessToken;
	private String member2AccessToken;

	@BeforeEach
	public void setUp() {
		super.setUp();
		String adminAccessToken = 로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD);
		member1AccessToken = 로그인_되어_있음(MEMBER_EMAIL, MEMBER_PASSWORD);
		member2AccessToken = 로그인_되어_있음(MEMBER_EMAIL2, MEMBER_PASSWORD2);
		강남역 = 지하철역_생성_요청("강남역", adminAccessToken).jsonPath().getLong("id");
		양재역 = 지하철역_생성_요청("양재역", adminAccessToken).jsonPath().getLong("id");
		삼성역 = 지하철역_생성_요청("삼성역", adminAccessToken).jsonPath().getLong("id");

		Map<String, String> lineCreateParams = createLineCreateParams(강남역, 양재역);
		지하철_노선_생성_요청(lineCreateParams, adminAccessToken).jsonPath().getLong("id");
	}

	/**
	 * Given 동일 사용자가 2개의 즐겨찾기를 등록하고
	 * When 조회하면
	 * Then 등록한 즐겨찾기 내역이 조회된다.
	 */
	@Test
	@DisplayName("즐겨찾기_등록_권한있는_경우")
	void registerFavorite() {
		//given
		즐겨찾기_등록(강남역, 양재역, member1AccessToken);
		즐겨찾기_등록(삼성역, 양재역, member1AccessToken);

		//when
		ExtractableResponse<Response> 즐겨찾기조회 = 즐겨찾기_조회(member1AccessToken);

		//then
		Assertions.assertThat(즐겨찾기조회.jsonPath().getList("source.id", Long.class))
			.hasSize(2)
			.containsExactly(강남역, 삼성역);
	}

	/**
	 * Given 각각 다른 사용자가 즐겨찾기 등록하고
	 * When 두번쨰 등록한 유저의 즐겨찾기를 조회하면
	 * Then 1개의 등록된 즐겨찾기가 조회된다.
	 */
	@Test
	@DisplayName("즐겨찾기_등록_각각_다른유저가_등록")
	void registerFavoriteByEachMember() {
		//given
		즐겨찾기_등록(강남역, 양재역, member1AccessToken);
		즐겨찾기_등록(삼성역, 양재역, member2AccessToken);

		//when
		ExtractableResponse<Response> 즐겨찾기조회 = 즐겨찾기_조회(member2AccessToken);

		//then
		Assertions.assertThat(즐겨찾기조회.jsonPath().getList("source.id", Long.class))
			.hasSize(1)
			.containsExactly(삼성역);
		Assertions.assertThat(즐겨찾기조회.jsonPath().getList("target.id", Long.class))
			.containsExactly(양재역);

	}

	/**
	 * Given 즐겨찾기를 등록하고
	 * When 등록한 즐겨 찾기를 삭제하면
	 * Then 즐겨찾기가 삭제된다
	 */
	@Test
	@DisplayName("즐겨찾기_삭제_권한있는_경우")
	void removeFavoriteByMember() {
		//given
		즐겨찾기_등록(강남역, 양재역, member1AccessToken);

		//when
		Long favoriteId = 즐겨찾기_조회(member1AccessToken).jsonPath().getList("id", Long.class).get(0);
		즐겨찾기_삭제(favoriteId, member1AccessToken);

		//then
		ExtractableResponse<Response> 즐겨찾기조회2 = 즐겨찾기_조회(member1AccessToken);
		assertThat(즐겨찾기조회2.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(즐겨찾기조회2.jsonPath().getList("id")).isEmpty();

	}

	/**
	 * Given 즐겨찾기를 등록하고
	 * When 다른 사용자가 해당 즐겨찾기를 삭제하면
	 * Then 400응답 수신
	 */
	@Test
	@DisplayName("즐겨찾기_삭제_권한없는_경우")
	void removeFavoriteByGuest() {
		//given
		즐겨찾기_등록(강남역, 양재역, member1AccessToken);

		//when
		Long favoriteId = 즐겨찾기_조회(member1AccessToken).jsonPath().getList("id", Long.class).get(0);

		//then
		assertThat(즐겨찾기_삭제(favoriteId, member2AccessToken).statusCode())
			.isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

}
