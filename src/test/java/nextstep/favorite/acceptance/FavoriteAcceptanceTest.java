package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.line.LineRequest;
import nextstep.line.LineResponse;
import nextstep.section.SectionRequest;
import nextstep.station.Station;
import nextstep.station.StationResponse;
import nextstep.utils.AcceptanceTest;
import nextstep.utils.RestAssuredAuthUtil;
import nextstep.utils.RestAssuredUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.member.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private Long 이호선;
    private Long 서초역;
    private Long 교대역;
    private Long 강남역;
    private Long 고속터미널역;

    private String email = "nextstep@gmail.com";
    private String password = "1234";
    private String accessToken;

    @BeforeEach
    public void setFixture() {
        서초역 = RestAssuredUtil.post(new Station(1L, "서초역"), "stations")
                .as(StationResponse.class).getId();
        교대역 = RestAssuredUtil.post(new Station(2L, "교대역"), "stations")
                .as(StationResponse.class).getId();
        강남역 = RestAssuredUtil.post(new Station(3L, "강남역"), "stations")
                .as(StationResponse.class).getId();
        고속터미널역 = RestAssuredUtil.post(new Station(4L, "고속터미널역"), "stations")
                .as(StationResponse.class).getId();

        이호선 = RestAssuredUtil.post(new LineRequest(1L, "2호선", "green", 10L, 서초역, 교대역), "/lines")
                .as(LineResponse.class).getId();

        RestAssuredUtil.post(new SectionRequest(교대역, 강남역, 15L), "/lines/" + 이호선 + "/sections");

        회원_생성_요청(email, password,  20);
        accessToken = 회원_로그인_요청(email, password).jsonPath().getString("accessToken");
    }

    /**
     * When 특정 유저가 즐겨찾기를 생성하면
     * Then 해당 유저가 즐겨찾기 목록 조회 시 생성한 즐겨찾기를 찾을 수 있다
     */
    @DisplayName("즐겨찾기를 등록한다.")
    @Test
    void createFavorite() {
        // when
        ExtractableResponse<Response> response
                = RestAssuredAuthUtil.post(new FavoriteRequest(서초역, 교대역), "/favorites", accessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        FavoriteResponse[] favorites
                = RestAssuredAuthUtil.get("/favorites", accessToken).as(FavoriteResponse[].class);

        assertThat(favorites.length).isEqualTo(1);
        assertThat(favorites[0].getSource().getName()).isEqualTo("서초역");
        assertThat(favorites[0].getTarget().getName()).isEqualTo("교대역");
    }

    /**
     * When 즐겨찾기 생성시 해당 역들이 연결되어 있지 않다면
     * Then IllegalArgumentExcetpion을 발생시킨다.
     */
    @DisplayName("즐겨찾기 생성시 해당 역들이 연결되어 있지 않다면 에러를 발생시킨다.")
    @Test
    void cantCreateFavoriteWhenStationNotConnected() {
        // when
         ExtractableResponse<Response> response = RestAssured.given().log().all()
                 .body(new FavoriteRequest(서초역, 고속터미널역))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().oauth2(accessToken)
                    .when().post("/favorites")
                    .then().log().all()
                    .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * When 즐겨찾기 등록시 로그인 되어있지 않다면
     * Then 즐겨찾기 등록에 실패한다.
     */
    @DisplayName("즐겨찾기 생성시 AccessToken이 없다면 에러를 발생시킨다.")
    @Test
    void cantCreateFavoriteWhenNotHaveAccessToken() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(new FavoriteRequest(서초역, 고속터미널역))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 2개의 즐겨찾기를 생성하고
     * When 즐겨찾기 목록을 조회하면
     * Then 2개의 즐겨찾기를 응답 받는다
     */
    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void findAllFavorites() {
        // given
        RestAssuredAuthUtil.post(new FavoriteRequest(서초역, 교대역), "/favorites", accessToken);
        RestAssuredAuthUtil.post(new FavoriteRequest(서초역, 강남역), "/favorites", accessToken);

        // when
        FavoriteResponse[] favorites
                = RestAssuredAuthUtil.get("/favorites", accessToken).as(FavoriteResponse[].class);

        // then
        assertThat(favorites.length).isEqualTo(2);
        assertThat(favorites[0].getSource().getName()).isEqualTo("서초역");
        assertThat(favorites[0].getTarget().getName()).isEqualTo("교대역");
        assertThat(favorites[1].getSource().getName()).isEqualTo("서초역");
        assertThat(favorites[1].getTarget().getName()).isEqualTo("강남역");
    }

    /**
     * Given 특정 유저가 즐겨찾기를 생성하고
     * When 해당 즐겨찾기를 삭제하면
     * Then 해당 유저가 즐겨찾기 목록 조회 시 생성한 즐겨찾기를 찾을 수 없다
     */
    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorites() {
        // when
        RestAssuredAuthUtil.post(new FavoriteRequest(서초역, 교대역), "/favorites", accessToken);

        // when
        RestAssuredAuthUtil.delete("/favorites/" + 1L, accessToken);

        // then
        FavoriteResponse[] favorites
                = RestAssuredAuthUtil.get("/favorites", accessToken).as(FavoriteResponse[].class);
        assertThat(favorites.length).isEqualTo(0);
    }
}