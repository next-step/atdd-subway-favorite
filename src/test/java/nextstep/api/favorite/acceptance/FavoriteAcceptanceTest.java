package nextstep.api.favorite.acceptance;

import static nextstep.fixture.FavoriteFixtureCreator.*;
import static nextstep.fixture.MemberFixtureCreator.*;
import static nextstep.fixture.SubwayScenarioFixtureCreator.*;
import static nextstep.fixture.TokenFixtureCreator.*;
import static nextstep.utils.resthelper.ExtractableResponseParser.*;
import static nextstep.utils.resthelper.FavoriteRequestExecutor.*;
import static nextstep.utils.resthelper.MemberRequestExecutor.*;
import static nextstep.utils.resthelper.TokenRequestExecutor.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestExecutionListeners;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.api.CommonAcceptanceTest;
import nextstep.api.favorite.application.model.dto.FavoriteResponse;
import nextstep.utils.securityutils.WithMockCustomUser;
import nextstep.utils.securityutils.WithMockCustomUserTestExecutionListener;

@DisplayName("즐겨찾기 관련 기능")
@TestExecutionListeners(listeners = {WithMockCustomUserTestExecutionListener.class})
public class FavoriteAcceptanceTest extends CommonAcceptanceTest {

	private String authorizationToken;

	@Test
	@DisplayName("즐겨찾기 생성")
	@WithMockCustomUser(email = "user@example.com", password = "password")
	void createFavorite() {
		// given
		createStation("교대역");
		createStation("두번째역");
		createStation("양재역");
		long testLineId = createLine("테스트 노선", 1L, 2L, 10L);
		createSection(testLineId, 2L, 3L, 10L);
		long sourceId = 1L;
		long targetId = 3L;

		// when
		ExtractableResponse<Response> response = executeCreateFavoriteRequest(authorizationToken, createFavoriteCreateRequest(sourceId, targetId));

		// then
		assertThat(response.statusCode()).isEqualTo(CREATED.value());
		assertThat(response.header("Location")).isNotEmpty();
	}

	@Test
	@DisplayName("즐겨찾기 조회")
	@WithMockCustomUser(email = "user@example.com", password = "password")
	void getFavorites() {
		// given
		createStation("교대역");
		createStation("두번째역");
		createStation("양재역");
		long testLineId = createLine("테스트 노선", 1L, 2L, 10L);
		createSection(testLineId, 2L, 3L, 10L);

		long sourceId = 1L;
		long targetId = 3L;
		executeCreateFavoriteRequest(authorizationToken, createFavoriteCreateRequest(sourceId, targetId));

		// when
		ExtractableResponse<Response> response = executeGetFavoritesRequest(authorizationToken);

		// then
		assertThat(response.statusCode()).isEqualTo(OK.value());
		List<FavoriteResponse> favorites = response.body().jsonPath().getList(".", FavoriteResponse.class);
		assertThat(favorites).isNotEmpty();
		FavoriteResponse favorite = favorites.get(0);
		assertThat(favorite.getSource().getId()).isEqualTo(sourceId);
		assertThat(favorite.getTarget().getId()).isEqualTo(targetId);
	}

	@Test
	@DisplayName("즐겨찾기 삭제")
	@WithMockCustomUser(email = "user@example.com", password = "password")
	void deleteFavorite() {
		// given
		createStation("교대역");
		createStation("두번째역");
		createStation("양재역");
		long testLineId = createLine("테스트 노선", 1L, 2L, 10L);
		createSection(testLineId, 2L, 3L, 10L);

		long sourceId = 1L;
		long targetId = 3L;
		ExtractableResponse<Response> createResponse = executeCreateFavoriteRequest(authorizationToken, createFavoriteCreateRequest(sourceId, targetId));
		Long favoriteId = Long.parseLong(createResponse.header("Location").split("/")[2]);

		// when
		ExtractableResponse<Response> deleteResponse = executeDeleteFavoriteRequest(authorizationToken, favoriteId);

		// then
		assertThat(deleteResponse.statusCode()).isEqualTo(NO_CONTENT.value());
	}

	/**
	 * given - 1번 유저와 2번 유저가 각각 즐겨찾기를 등록하고
	 * when - 2번 유저가 1번 유저가 저장한 즐겨찾기를 삭제하려고 할때
	 * then - 403 에러 발생
	 */
	@Test
	@DisplayName("내가 등록하지 않은 즐겨찾기 삭제 시도")
	@WithMockCustomUser(email = "user@example.com", password = "password")
	void deleteUnownedFavorite() {
		// given
		createStation("Station A");
		createStation("Station B");
		createLine("테스트 노선", 1L, 2L, 10L);
		ExtractableResponse<Response> createResponse = executeCreateFavoriteRequest(authorizationToken, createFavoriteCreateRequest(1L, 2L));
		Long favoriteId = Long.parseLong(createResponse.header("Location").split("/")[2]);

		// when
		createMember(createMemberRequest("user2@example.com", "password2", 20));
		String anotherUserToken = parseAsAccessTokenWithBearer(loginAndCreateAuthorizationToken(createTokenRequest("user2@example.com", "password2")));
		ExtractableResponse<Response> deleteResponse = executeDeleteFavoriteRequest(anotherUserToken, favoriteId);

		// then
		assertThat(deleteResponse.statusCode()).isEqualTo(FORBIDDEN.value());
	}

	public void setAuthorizationToken(String authorizationToken) {
		this.authorizationToken = authorizationToken;
	}
}