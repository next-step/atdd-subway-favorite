package subway.acceptance.favorite;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.acceptance.auth.AuthFixture;
import subway.acceptance.member.MemberSteps;
import subway.acceptance.path.PathFixture;
import subway.acceptance.station.StationFixture;
import subway.exception.SubwayBadRequestException;
import subway.utils.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    /**
     * 즐겨찾기 생성
     */
    @DisplayName("즐겨찾기를 만든다.")
    @Test
    void createFavorite() {
        // given
        Long 강남역_ID = StationFixture.getStationId("강남역");
        Long 남부터미널역_ID = StationFixture.getStationId("남부터미널역");
        var request = FavoriteFixture.즐겨찾기_등록_요청(강남역_ID, 남부터미널역_ID);

        // when
        var response = FavoriteSteps.즐겨찾기_생성_API(accessToken, request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * 즐겨찾기 조회
     */
    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void retrieveFavorites() {
        // given
        FavoriteFixture.강남역_남부터미널역_즐겨찾기_등록(accessToken);

        // when
        var response = FavoriteSteps.즐겨찾기_조회_API(accessToken);

        // then
//        response.body().jsonPath(). // TODO : 이거 짜야됨
    }

    /**
     * 즐겨찾기 삭제
     */
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

    /**
     * 이어지지 않는 경로로 생성을 할 수 없다. (문서 스팩)
     */
    @DisplayName("이어지지 않는 경로로 즐겨찾기를 생성 할 수 없다.")
    @Test
    void createFavoriteWithNotConnectedSections() {
        // given
        Long 강남역_ID = StationFixture.getStationId("강남역");
        Long 왕십리역_ID = StationFixture.getStationId("왕십리역");
        var request = FavoriteFixture.즐겨찾기_등록_요청(강남역_ID, 왕십리역_ID);

        // when
        var response = FavoriteSteps.즐겨찾기_생성_API(accessToken, request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * 없는 즐겨찾기를 삭제할 수 없다.
     */
    @DisplayName("없는 즐겨찾기를 삭제할 수 없다.")
    @Test
    void removeFavoriteWithNotExist() {
        // when/then
        assertThatThrownBy(() -> FavoriteSteps.즐겨찾기_삭제_API(accessToken, "999"))
                .isInstanceOf(SubwayBadRequestException.class);
    }


    /**
     * 자신의 소유가 아닌 즐겨찾기는 삭제 할 수 없다.
     */
    @DisplayName("자신의 소유가 아닌 즐겨찾기는 삭제 할 수 없다.")
    @Test
    void removeFavoriteWithNotOwner() {
        // given
        final String emailAnotherMember = "email2@email.com";
        final String passwordAnotherMember = "password2";
        final int ageAnotherMember = 30;
        MemberSteps.회원_생성_요청(emailAnotherMember, passwordAnotherMember, ageAnotherMember);
        var response = AuthFixture.로그인_호출(emailAnotherMember, passwordAnotherMember);
        var accessTokenAnotherMember = response.jsonPath().getString("accessToken");

        FavoriteFixture.강남역_남부터미널역_즐겨찾기_등록(accessToken);
        var retrieveResponse = FavoriteSteps.즐겨찾기_조회_API(accessToken);
        var ids = retrieveResponse.body().jsonPath().getList("id", String.class);
        var id = ids.get(0);

        // when/then
        assertThatThrownBy(() -> FavoriteSteps.즐겨찾기_삭제_API(accessTokenAnotherMember, id))
                .isInstanceOf(SubwayBadRequestException.class); // TODO : UNAUTHORIZED 어떻게좀 해보기
    }

}
