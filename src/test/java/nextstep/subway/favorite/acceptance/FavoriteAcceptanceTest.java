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
import static nextstep.subway.member.MemberSteps.회원등록_되어_있음;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;

@DisplayName("지하철 즐겨찾기 인수테스트")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 남부터미널역;

    private TokenResponse boramToken;
    private TokenResponse heesunToken;

    private Map<String, String> favoriteParams1;
    private Map<String, String> favoriteParams2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        지하철_노선_등록되어_있음(createLineParam("2호선", "green", 교대역, 강남역, 5));
        지하철_노선_등록되어_있음(createLineParam("신분당선", "green", 강남역, 양재역, 10));
        지하철_노선_등록되어_있음(createLineParam("3호선", "green", 교대역, 남부터미널역, 2));

        회원등록_되어_있음("boram@gmail.com", "test1234", 15);
        boramToken = 로그인_되어_있음("boram@gmail.com", "test1234");

        회원등록_되어_있음("heesun@gmail.com", "test1234", 20);
        heesunToken = 로그인_되어_있음("heesun@gmail.com", "test1234");

        favoriteParams1 = createFavoriteParam(교대역, 강남역);
        favoriteParams2 = createFavoriteParam(강남역, 양재역);
    }

    @DisplayName("지하철 즐겨찾기를 생성")
    @Test
    void createFavorite() {
        // when
        ExtractableResponse<Response> response = 지하철_즐겨찾기_생성_요청(boramToken.getAccessToken(), favoriteParams1);

        // then
        지하철_즐겨찾기_생성됨(response);
    }

    @DisplayName("지하철 즐겨찾기 생성 요청 실패")
    @Test
    void createFavoriteWithNotExistedStation() {
        // given
        Map<String, String> favoriteParams = new HashMap<>();
        favoriteParams.put("source", "-1");
        favoriteParams.put("target", String.valueOf(강남역.getId()));

        // when
        ExtractableResponse<Response> response = 지하철_즐겨찾기_생성_요청(boramToken.getAccessToken(), favoriteParams);

        // then
        지하철_즐겨찾기_생성_실패됨(response);
    }

    @DisplayName("지하철 즐겨찾기 목록을 조회")
    @Test
    void getFavorites() {
        // given
        지하철_즐겨찾기_생성_요청(boramToken.getAccessToken(), favoriteParams1);
        지하철_즐겨찾기_생성_요청(boramToken.getAccessToken(), favoriteParams2);

        // when
        ExtractableResponse<Response> response = 지하철_즐겨찾기_조회_요청(boramToken.getAccessToken());

        // then
        지하철_즐겨찾기_목록_조회됨(response);
    }

    @DisplayName("지하철 즐겨찾기를 삭제")
    @Test
    void deleteFavorite() {
        // given
        Long favoriteId = 지하철_즐겨찾기_생성_요청(boramToken.getAccessToken(), favoriteParams1).as(FavoriteResponse.class).getId();

        // when
        ExtractableResponse<Response> response = 지하철_즐겨찾기_삭제_요청(boramToken.getAccessToken(), favoriteId);

        // then
        지하철_즐겨찾기_삭제됨(response);
    }

    @DisplayName("유효하지 않은 토큰을 사용해 지하철 즐겨찾기를 삭제")
    @Test
    void getFavoritesWithWrongToken() {
        // given
        Long favoriteId = 지하철_즐겨찾기_생성_요청(boramToken.getAccessToken(), favoriteParams1).as(FavoriteResponse.class).getId();

        // when
        ExtractableResponse<Response> response = 지하철_즐겨찾기_삭제_요청("errToken", favoriteId);

        // then
        지하철_즐겨찾기_삭제_실패함(response);
    }

    @DisplayName("다른 유저 토큰을 사용해 지하철 즐겨찾기를 삭제")
    @Test
    void getFavoritesByOtherUserToken() {
        // given
        Long favoriteId = 지하철_즐겨찾기_생성_요청(boramToken.getAccessToken(), favoriteParams1).as(FavoriteResponse.class).getId();

        // when
        ExtractableResponse<Response> response = 지하철_즐겨찾기_삭제_요청(heesunToken.getAccessToken(), favoriteId);

        // then
        지하철_즐겨찾기_삭제_실패함(response);
    }

    private Map<String, String> createLineParam(String name, String color, StationResponse upStation, StationResponse downStation, Integer distance){
        Map<String, String> params = new HashMap<>();
        params.put("name",  name);
        params.put("color", color);
        params.put("upStationId", String.valueOf(upStation.getId()));
        params.put("downStationId", String.valueOf(downStation.getId()));
        params.put("distance", String.valueOf(distance));

        return params;
    }

    private Map<String, String> createFavoriteParam(StationResponse source, StationResponse target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(source.getId()));
        params.put("target", String.valueOf(target.getId()));
        return params;
    }


}
