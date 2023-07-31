package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("즐겨찾기 관리 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    private Long 교대역;
    private Long 양재역;

    @BeforeEach
    void setup() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");

        회원_생성_요청(EMAIL, PASSWORD, AGE);
    }

    /**
     * Given 로그인한 상태에서
     * When 즐겨찾기 생성하면
     * Then 성공 응답한다
     */
    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavorite() {
        // given
        var 토큰 = 토큰_추출(로그인(EMAIL, PASSWORD));

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성(토큰, 교대역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 로그인하지 않은 상태에서
     * When 즐겨찾기 생성하면
     * Then 에러 응답한다
     */
    @DisplayName("로그인하지 않은 상태에서 즐겨찾기 생성")
    @Test
    void createFavoriteWithoutLogin() {
        // given
        var 토큰 = "";

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성(토큰, 교대역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 로그인한 상태에서
     * When 존재하지 않은 역으로 즐겨찾기 생성하면
     * Then 에러 응답한다
     */
    @DisplayName("존재하지 않은 역으로 즐겨찾기 생성")
    @Test
    void createFavoriteByNoStationId() {
        // given
        var 토큰 = 토큰_추출(로그인(EMAIL, PASSWORD));

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성(토큰, 0L, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 로그인하고 즐겨찾기 생성하고
     * When 즐겨찾기 조회하면
     * Then 등록된 즐겨찾기 목록이 조회된다
     */
    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void getFavorites() {
        // given
        var 토큰 = 토큰_추출(로그인(EMAIL, PASSWORD));
        즐겨찾기_생성(토큰, 교대역, 양재역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회(토큰);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<FavoriteResponse> favorites = 즐겨찾기_목록_추출(response);
        assertTrue(favorites.stream().anyMatch(f -> f.getSource().getId().equals(교대역) && f.getTarget().getId().equals(양재역)));
    }

    /**
     * Given 로그인하고 즐겨찾기 생성하고
     * When 즐겨찾기 삭제하면
     * Then 성공 응답한다
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        // given
        var 토큰 = 토큰_추출(로그인(EMAIL, PASSWORD));
        즐겨찾기_생성(토큰, 교대역, 양재역);
        var 즐겨찾기_아이디 = 즐겨찾기_목록_추출(즐겨찾기_목록_조회(토큰)).get(0).getId();

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제(토큰, 즐겨찾기_아이디);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 로그인(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
    }

    private String 토큰_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("accessToken");
    }

    private List<FavoriteResponse> 즐겨찾기_목록_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", FavoriteResponse.class);
    }
}
