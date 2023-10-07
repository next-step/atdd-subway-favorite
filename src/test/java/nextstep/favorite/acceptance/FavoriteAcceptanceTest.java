package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.study.AuthSteps;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.favorite.acceptance.FavoriteSteps.*;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.StationSteps.역_생성_ID_추출;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    Long 역삼역_ID;
    Long 선릉역_ID;

    String accessToken;

    /**
     * Given 역이 생성되어 있다.
     * Given 유저가 생성되어 있다.
     * Given 유저가 로그인되어 있다.
     */
    @BeforeEach
    public void setUp() {
        String EMAIL = "email@email.com";
        String PASSWORD = "password";
        Integer age = 12;
        // given
        역삼역_ID = 역_생성_ID_추출(지하철역_생성_요청("역삼역"));
        선릉역_ID = 역_생성_ID_추출(지하철역_생성_요청("선릉역"));
        // given
        회원_생성_요청(EMAIL, PASSWORD, age);
        accessToken = AuthSteps.bearer_로그인_AccessToken_추출(EMAIL, PASSWORD);
    }

    /**
     * When 즐겨찾기 시작역과 도착역을 등록한다.
     * Then 즐겨찾기가 생성된다.
     */
    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(역삼역_ID, 선릉역_ID, accessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 즐겨찾기가 생성된다.
     * When 즐겨찾기를 조회한다.
     * Then 시작역과 도착역이 조회된다.
     */
    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void searchFavorite() {
        // given
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(역삼역_ID, 선릉역_ID, accessToken);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(createResponse, accessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(즐겨찾기_조회_출발역ID_추출(response)).isEqualTo(역삼역_ID);
        assertThat(즐겨찾기_조회_도착역ID_추출(response)).isEqualTo(선릉역_ID);
    }

    /**
     * Given 즐겨찾기가 생성된다.
     * When 로그인 없이 즐겨찾기를 조회한다.
     * Then 권한이 없으므로 실패한다.
     */
    @DisplayName("비로그인으로 즐겨찾기 조회시 실패한다.")
    @Test
    void searchFavoriteNoLogin() {
        // given
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(역삼역_ID, 선릉역_ID, accessToken);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> 비로그인으로_즐겨찾기_조회_요청 = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all().extract();

        // then
        assertThat(비로그인으로_즐겨찾기_조회_요청.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 유저1가 즐겨찾기1를 생성한다.
     * Given 유저2가 즐겨찾기2를 생성한다.
     * When 유저1이 즐겨찾기2를 조회한다.
     * Then 권한이 없으므로 실패한다.
     */
    @DisplayName("다른 유저의 즐겨찾기 조회시 실패한다.")
    @Test
    void searchFavoriteAnotherMember() {
        // given
        String EMAIL2 = "email2@email.com";
        String PASSWORD2 = "password2";
        회원_생성_요청(EMAIL2, PASSWORD2, 14);
        String accessToken2 = AuthSteps.bearer_로그인_AccessToken_추출(EMAIL2, PASSWORD2);
        // given
        역삼역_ID = 역_생성_ID_추출(지하철역_생성_요청("역삼역"));
        선릉역_ID = 역_생성_ID_추출(지하철역_생성_요청("선릉역"));

        // given
        ExtractableResponse<Response> 유저1의_즐겨찾기_생성요청 = 즐겨찾기_생성_요청(역삼역_ID, 선릉역_ID, accessToken);
        ExtractableResponse<Response> 유저2의_즐겨찾기_생성요청 = 즐겨찾기_생성_요청(역삼역_ID, 선릉역_ID, accessToken2);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(유저2의_즐겨찾기_생성요청, accessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 즐겨찾기가 생성된다.
     * When 즐겨찾기를 삭제한다.
     * Then 즐겨찾기가 삭제된다.
     */
    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {
        // given
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(역삼역_ID, 선릉역_ID, accessToken);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(createResponse, accessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 즐겨찾기가 생성된다.
     * When 로그인 없이 즐겨찾기를 삭제한다.
     * Then 권한이 없으므로 실패한다.
     */
    @DisplayName("비로그인으로 즐겨찾기 삭제시 실패한다.")
    @Test
    void deleteFavoriteNoLogin() {
        // given
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(역삼역_ID, 선릉역_ID, accessToken);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> 비로그인으로_즐겨찾기_삭제_요청 = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(uri)
                .then().log().all().extract();

        // then
        assertThat(비로그인으로_즐겨찾기_삭제_요청.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 유저1가 즐겨찾기1를 생성한다.
     * Given 유저2가 즐겨찾기2를 생성한다.
     * When 유저1이 즐겨찾기2를 삭제한다.
     * Then 권한이 없으므로 실패한다.
     */
    @DisplayName("다른 유저의 즐겨찾기 삭제시 실패한다.")
    @Test
    void deleteFavoriteAnotherMember() {
        // given
        String EMAIL2 = "email2@email.com";
        String PASSWORD2 = "password2";
        회원_생성_요청(EMAIL2, PASSWORD2, 14);
        String accessToken2 = AuthSteps.bearer_로그인_AccessToken_추출(EMAIL2, PASSWORD2);
        // given
        역삼역_ID = 역_생성_ID_추출(지하철역_생성_요청("역삼역"));
        선릉역_ID = 역_생성_ID_추출(지하철역_생성_요청("선릉역"));

        // given
        ExtractableResponse<Response> 유저1의_즐겨찾기_생성요청 = 즐겨찾기_생성_요청(역삼역_ID, 선릉역_ID, accessToken);
        ExtractableResponse<Response> 유저2의_즐겨찾기_생성요청 = 즐겨찾기_생성_요청(역삼역_ID, 선릉역_ID, accessToken2);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(유저2의_즐겨찾기_생성요청, accessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
