package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.favorite.acceptance.FavoriteSteps.*;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.member.MemberSteps.회원_등록되어_있음;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 남부터미널역;

    private TokenResponse 토큰;

    private Map<String, String> favoriteParams1;
    private Map<String, String> favoriteParams2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        지하철_노선_등록되어_있음("2호선", "green", 교대역, 강남역, 10);
        지하철_노선_등록되어_있음("신분당선", "green", 강남역, 양재역, 10);
        지하철_노선_등록되어_있음("3호선", "green", 교대역, 남부터미널역, 2);

        회원_등록되어_있음("test@gmail.com", "test1234", 20);
        토큰 = 로그인_되어_있음("test@gmail.com", "test1234");

        favoriteParams1 = new HashMap<>();
        favoriteParams1.put("source", String.valueOf(교대역.getId()));
        favoriteParams1.put("target", String.valueOf(강남역.getId()));

        favoriteParams2 = new HashMap<>();
        favoriteParams2.put("source", String.valueOf(강남역.getId()));
        favoriteParams2.put("target", String.valueOf(양재역.getId()));
    }


    @DisplayName("지하철 즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        // when
        ExtractableResponse<Response> response = 지하철_즐겨찾기_생성_요청(토큰.getAccessToken(), favoriteParams1);

        // then
        지하철_즐겨찾기_생성됨(response);
    }

    @DisplayName("지하철 즐겨찾기 목록을 조회한다.")
    @Test
    void getFavorites() {
        // given
        지하철_즐겨찾기_생성_요청(토큰.getAccessToken(), favoriteParams1);
        지하철_즐겨찾기_생성_요청(토큰.getAccessToken(), favoriteParams2);

        // when
        ExtractableResponse<Response> response = 지하철_즐겨찾기_조회_요청(토큰.getAccessToken());

        // then
        지하철_즐겨찾기_목록_조회됨(response);
    }

    @DisplayName("지하철 즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {
        // given
        Long favoriteId = 지하철_즐겨찾기_생성_요청(토큰.getAccessToken(), favoriteParams1).as(FavoriteResponse.class).getId();

        // when
        ExtractableResponse<Response> response = 지하철_즐겨찾기_삭제_요청(토큰.getAccessToken(), favoriteId);

        // then
        지하철_즐겨찾기_삭제됨(response);
    }
}
