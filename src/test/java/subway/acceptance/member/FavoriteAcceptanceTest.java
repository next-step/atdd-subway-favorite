package subway.acceptance.member;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.acceptance.auth.AuthFixture;
import subway.acceptance.path.PathFixture;
import subway.acceptance.station.StationFixture;
import subway.utils.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    private String accessToken = "";

    @BeforeEach
    void beforeEach() {
        StationFixture.기본_역_생성_호출();
        PathFixture.이호선_삼호선_신분당선_A호선_생성_호출();
        final String email = "email@email.com";
        final String password = "password";
        final int age = 20;
        MemberSteps.회원_생성_요청(email, password, age);
        var response = AuthFixture.로그인_호출(email, password);
        accessToken = response.jsonPath().getString("accessToken");

    }

    @DisplayName("즐겨찾기를 만든다.")
    @Test
    void createFavorite() {
        // given
        Long 강남역_ID = StationFixture.getStationId("강남역");
        Long 남부터미널역_ID = StationFixture.getStationId("남부터미널역");
        var request = FavoriteFixture.즐겨찾기_등록_요청_만들기(강남역_ID, 남부터미널역_ID);

        // when
        var response = FavoriteSteps.즐겨찾기_생성_API(accessToken, request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void retrieveFavorites() {
        // given
        FavoriteFixture.강남역_남부터미널역_즐겨찾기_등록(accessToken);

        // when
        var response = FavoriteSteps.즐겨찾기_조회_API(accessToken);

        // then
        assertThat(response.jsonPath().getString("source[0].name")).isEqualTo("강남역");
        assertThat(response.jsonPath().getString("target[0].name")).isEqualTo("남부터미널역");
    }

    @DisplayName("인가되지 않은 사용자가 즐겨찾기를 조회한다.")
    @Test
    void retrieveFavoritesWithoutAuthorization() {
        // given
        FavoriteFixture.강남역_남부터미널역_즐겨찾기_등록(accessToken);

        // when
        var response = FavoriteSteps.즐겨찾기_조회_인가제외_API();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void removeFavorite() {
        // given
        FavoriteFixture.강남역_남부터미널역_즐겨찾기_등록(accessToken);
        var retrieveResponse = FavoriteSteps.즐겨찾기_조회_API(accessToken);
        var ids = retrieveResponse.body().jsonPath().getList("id", String.class);
        var id = ids.get(0);

        // when
        var response = FavoriteSteps.즐겨찾기_삭제_API(accessToken, id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("이어지지 않는 경로로 즐겨찾기를 생성 할 수 없다.")
    @Test
    void createFavoriteWithNotConnectedSections() {
        // given
        Long 강남역_ID = StationFixture.getStationId("강남역");
        Long 왕십리역_ID = StationFixture.getStationId("왕십리역");
        var request = FavoriteFixture.즐겨찾기_등록_요청_만들기(강남역_ID, 왕십리역_ID);

        // when
        var response = FavoriteSteps.즐겨찾기_생성_API(accessToken, request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("없는 즐겨찾기를 삭제할 수 없다.")
    @Test
    void removeFavoriteWithNotExist() {
        // when
        var response = FavoriteSteps.즐겨찾기_삭제_API(accessToken, "999");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given 두 회원이 있고
     * Given 회원이 즐겨찾기를 등록하고
     * When 다른 회원이 그 즐겨찾기를 제거하려 하면
     * Then 제거 할 수 없다.
     */
    @DisplayName("자신의 소유가 아닌 즐겨찾기는 삭제 할 수 없다.")
    @Test
    void removeFavoriteWithNotOwner() {
        // given
        final String emailAnotherMember = "email2@email.com";
        final String passwordAnotherMember = "password2";
        final int ageAnotherMember = 30;
        MemberSteps.회원_생성_요청(emailAnotherMember, passwordAnotherMember, ageAnotherMember);
        var loginResponse = AuthFixture.로그인_호출(emailAnotherMember, passwordAnotherMember);
        var accessTokenAnotherMember = loginResponse.jsonPath().getString("accessToken");

        FavoriteFixture.강남역_남부터미널역_즐겨찾기_등록(accessToken);
        var retrieveResponse = FavoriteSteps.즐겨찾기_조회_API(accessToken);
        var ids = retrieveResponse.body().jsonPath().getList("id", String.class);
        var id = ids.get(0);

        // when
        var response = FavoriteSteps.즐겨찾기_삭제_API(accessTokenAnotherMember, id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
