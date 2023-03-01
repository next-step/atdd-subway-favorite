package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class FavoriteAcceptanceTest extends AcceptanceTest {
    private Long 강남역;
    private Long 양재역;
    private Long 양재시민의숲역;
    private Long 청계산입구역;

    private String accessToken;

    /**
     * GIVEN 지하철 역을 두개 생성하고
     * GIVEN 회원가입 후 로그인하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        양재시민의숲역 = 지하철역_생성_요청("양재시민의숲역").jsonPath().getLong("id");
        청계산입구역 = 지하철역_생성_요청("청계산입구역").jsonPath().getLong("id");

        회원_생성_요청("bactoria@gmail.com", "qwe123", 20);
        var 베어러_인증_로그인_응답 = 베어러_인증_로그인_요청("bactoria@gmail.com", "qwe123");
        accessToken = Access_Token을_가져온다(베어러_인증_로그인_응답);
    }

    /**
     * WHEN 로그인한 회원이 출발역과 도착역을 즐겨찾기 하는 경우 <br>
     * THEN 즐겨찾기가 추가된다 <br>
     */
    @DisplayName("로그인한 유저가 즐겨찾기 추가")
    @Test
    void addFavoriteLoginUser() {
        // when
        var 즐겨찾기_추가_응답 = 즐겨찾기_추가_요청(accessToken, 강남역, 양재역);

        // then
        assertThat(즐겨찾기_추가_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * WHEN 로그인하지 않거나, 유효하지 않은 회원 인증정보로 출발역과 도착역을 즐겨찾기 하는 경우 <br>
     * THEN 예외가 발생한다 <br>
     */
    @DisplayName("로그인 하지 않은 유저가 즐겨찾기 추가")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"Bearer abcd"})
    void addFavoriteNotLoginUser(String illegalAccessToken) {
        // when
        var 즐겨찾기_추가_응답 = 즐겨찾기_추가_요청(illegalAccessToken, 강남역, 양재역);

        // then
        assertThat(즐겨찾기_추가_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * GIVEN 로그인한 회원이 즐겨찾기를 2번 하고
     * WHEN 로그인한 회원이 즐겨찾기 조회하는 경우 <br>
     * THEN 2개의 즐겨찾기 목록이 조회된다 <br>
     */
    @DisplayName("로그인한 유저가 즐겨찾기 조회")
    @Test
    void readFavoriteLoginUser() {
        // given
        즐겨찾기_추가_요청(accessToken, 강남역, 양재역);
        즐겨찾기_추가_요청(accessToken, 양재시민의숲역, 청계산입구역);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when()
                .get("/favorites")
                .then().log().all()
                .extract();

        // then
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.body().jsonPath().getList("source.id", Long.class)).containsExactly(강남역, 양재시민의숲역);
            assertThat(response.body().jsonPath().getList("target.id", Long.class)).containsExactly(양재역, 청계산입구역);
        });
    }

    /**
     * WHEN 로그인하지 않거나, 유효하지 않은 회원 인증정보로 즐겨찾기 조회하는 경우 <br>
     * THEN 예외가 발생한다 <br>
     */
    @DisplayName("로그인하지 않은 유저가 즐겨찾기 조회")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"Bearer abcd"})
    void readFavoriteNotLoginUser(String illegalAccessToken) {
        // when
        var given = RestAssured.given().log().all();

        if (illegalAccessToken != null) {
            given.auth().oauth2(illegalAccessToken);
        }

        var 즐겨찾기_조회_응답 = given.when()
                .get("/favorites")
                .then().log().all()
                .extract();

        // then
        assertThat(즐겨찾기_조회_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * GIVEN 로그인한 회원이 즐겨찾기를 하고 <br>
     * WHEN 즐겨찾기를 삭제하는 경우 <br>
     * THEN 즐겨찾기가 삭제된다 <br>
     */
    @DisplayName("로그인한 유저가 즐겨찾기 삭제")
    @Test
    void deleteFavoriteLoginUser() {
        // given
        var 즐겨찾기_추가_응답 = 즐겨찾기_추가_요청(accessToken, 강남역, 양재역);
        String location = 즐겨찾기_추가_응답.header("location");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(location)
                .then().log().all()
                .extract();
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * WHEN 로그인하지 않거나, 유효하지 않은 회원 인증정보로 즐겨찾기 삭제하는 경우 <br>
     * THEN 예외가 발생한다 <br>
     */
    @DisplayName("로그인하지 않은 유저가 즐겨찾기 삭제")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"Bearer abcd"})
    void deleteFavoriteNotLoginUser(String illegalAccessToken) {
        // given
        var 즐겨찾기_추가_응답 = 즐겨찾기_추가_요청(accessToken, 강남역, 양재역);
        String location = 즐겨찾기_추가_응답.header("location");

        // when
        var given = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        if (illegalAccessToken != null) {
            given.auth().oauth2(illegalAccessToken);
        }

        var 즐겨찾기_삭제_응답 = given.when()
                .delete(location)
                .then().log().all()
                .extract();

        // then
        assertThat(즐겨찾기_삭제_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * WHEN 로그인한 유저가 존재하지 않는 즐겨찾기를 삭제하는 경우 <br>
     * THEN 예외가 발생한다 <br>
     */
    @DisplayName("로그인한 유저가 존재하지 않는 즐겨찾기 삭제")
    @Test
    void deleteNotExistsFavorite() {
        // given
        Long 존재하지_않는_즐겨찾기_아이디 = Long.MAX_VALUE;

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/favorites/" + 존재하지_않는_즐겨찾기_아이디)
                .then().log().all()
                .extract();
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_추가_요청(String accessToken, Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source + "");
        params.put("target", target + "");

        var given = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        if (accessToken != null) {
            given = given.auth().oauth2(accessToken);
        }

        // when
        return given
                .when()
                .post("/favorites")
                .then().log().all()
                .extract();
    }
}
