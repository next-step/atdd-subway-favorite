package nextstep.subway.acceptance.favorites;

import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static nextstep.subway.acceptance.favorites.FavoritesSteps.*;
import static nextstep.subway.acceptance.member.MemberSteps.*;
import static nextstep.subway.acceptance.station.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.*;

@DisplayName("즐겨찾기 기능 인수테스트")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private Long 교대역;
    private Long 양재역;

    @BeforeEach
    void before() {
        회원_생성_요청(EMAIL, PASSWORD, 20);
        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
    }

    @DisplayName("로그인이 성공한 회원은 즐겨찾기 등록을 시도하면 성공한다.")
    @Test
    void favoritePostTest() {
        // given
        var 베어러_인증_로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_AccessToken_가져오기(베어러_인증_로그인_요청);

        // when
        var response = 즐겨찾기_등록_요청(유효한_토큰, 교대역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }

    @DisplayName("즐겨찾기에 추가할 지하철 역이 존재하지 않으면 즐겨찾기 등록에 실패한다.")
    @Test
    void favoritePostTest2() {
        // given
        var 베어러_인증_로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_AccessToken_가져오기(베어러_인증_로그인_요청);
        Long 부발역 = 3L;
        Long 판교역 = 4L;

        // when
        var response = 즐겨찾기_등록_요청(유효한_토큰, 부발역, 판교역);

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }

    @DisplayName("로그인을 하지 않은 회원은 즐겨찾기 등록을 시도하면 실패한다.")
    @Test
    void favoritePostExceptionTest() {
        // when
        var 유효하지_않은_토큰 = "kkkkkkkkkkkk";
        var response = 즐겨찾기_등록_요청(유효하지_않은_토큰, 교대역, 양재역);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().getString("code")).isEqualTo(String.valueOf(UNAUTHORIZED.value()))
        );
    }

    @DisplayName("로그인을 성공한 회원은 즐겨찾기 조회를 시도하면 성공한다.")
    @Test
    void favoritesGetTest() {
        // given
        var 베어러_인증_로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_AccessToken_가져오기(베어러_인증_로그인_요청);

        // when
        var response = 즐겨찾기_조회_요청(유효한_토큰);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    @DisplayName("로그인을 하지 않은 회원은 즐겨찾기 조회를 시도하면 실패한다.")
    @Test
    void favoritesGetExceptionTest() {
        // when
        var 유효하지_않은_토큰 = "kkkkkkkkkkkk";
        var response = 즐겨찾기_조회_요청(유효하지_않은_토큰);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().getString("code")).isEqualTo(String.valueOf(UNAUTHORIZED.value()))
        );
    }

    @DisplayName("로그인을 성공한 회원은 즐겨찾기 삭제를 시도하면 성공한다.")
    @Test
    void favoritesDeleteTest() {
        // given
        var 베어러_인증_로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_AccessToken_가져오기(베어러_인증_로그인_요청);
        var id = 즐겨찾기_등록_요청(유효한_토큰, 교대역, 양재역).jsonPath().getLong("id");

        // when
        var response = 즐겨찾기_삭제_요청(유효한_토큰, id);

        // then
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }

    @DisplayName("자기 자신의 즐겨찾기가 아닐 경우 즐겨찾기 제거에 실패한다.")
    @Test
    void favoriteDeleteExceptionTest() {
        // given
        var 베어러_인증_로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_AccessToken_가져오기(베어러_인증_로그인_요청);
        var id = 즐겨찾기_등록_요청(유효한_토큰, 교대역, 양재역).jsonPath().getLong("id");

        회원_생성_요청("jisoo@gmail.com", "1234566", 20);
        var 베어러_또다른_인증_로그인_요청 = 베어러_인증_로그인_요청("jisoo@gmail.com", "1234566");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_AccessToken_가져오기(베어러_또다른_인증_로그인_요청);

        // when
        var response = 즐겨찾기_삭제_요청(다른_유효한_토큰, id);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().getString("code")).isEqualTo(String.valueOf(UNAUTHORIZED.value()))
        );
    }



}
