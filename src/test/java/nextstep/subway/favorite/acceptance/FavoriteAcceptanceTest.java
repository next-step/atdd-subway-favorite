package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.favorite.acceptance.FavoriteSteps.*;
import static nextstep.subway.line.acceptance.LineSteps.*;
import static nextstep.subway.member.MemberSteps.*;
import static nextstep.subway.station.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 테스트")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    private String 토큰;
    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 교대역;
    private StationResponse 고속버스터미널역;
    private LineResponse 이호선;

    @BeforeEach
    void beforeEach() {
        super.setUp();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        고속버스터미널역 = 지하철역_등록되어_있음("고속버스터미널역").as(StationResponse.class);
        이호선 = 지하철_노선_등록되어_있음(createLine("이호선", "green", 강남역, 역삼역, 10)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청되어_있음(이호선, 역삼역, 교대역, 5);
        지하철_노선에_지하철역_등록_요청되어_있음(이호선, 교대역, 고속버스터미널역, 3);
        회원_생성_되어_있음(EMAIL, PASSWORD, AGE);
        토큰 = 로그인_되어_있음(EMAIL, PASSWORD).getAccessToken();
    }

    @DisplayName("즐겨찾기를 생성한다")
    @Test
    void createFavorite() {
        // given
        Long source = 1L;
        Long target = 3L;

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(source, target, 토큰);

        // then
        즐겨찾기_생성됨(response);
    }

    @DisplayName("즐겨찾기 목록을 조회한다")
    @Test
    void findFavorite() {
        // given
        즐겨찾기_생성_요청되어_있음(1L, 3L, 토큰);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(토큰);

        // then
        즐겨찾기_목록_조회됨(response);
    }

    @DisplayName("즐겨찾기를 삭제한다")
    @Test
    void deleteFavorite() {
        // given
        즐겨찾기_생성_요청되어_있음(1L, 3L, 토큰);
        Long favoriteId = 1L;

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .auth().oauth2(토큰)
                .when().delete("/favorites/{favoriteId}", favoriteId)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("비로그인 상태에서 즐겨찾기 조회한다")
    @Test
    void unAuthorized() {
        // given
        즐겨찾기_생성_요청되어_있음(1L, 3L, 토큰);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_실패_요청();

        // then를
        즐겨찾기_조회_실패됨(response);
    }

    private void 즐겨찾기_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private Map<String, String> createLine(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("color", color);
        map.put("upStationId", upStation.getId() + "");
        map.put("downStationId", downStation.getId() + "");
        map.put("distance", distance +"");
        return map;
    }
}
