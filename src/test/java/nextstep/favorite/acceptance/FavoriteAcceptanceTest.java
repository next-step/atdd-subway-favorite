package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.subway.acceptance.annotation.AcceptanceTest;
import nextstep.subway.acceptance.line.LineApiRequester;
import nextstep.subway.acceptance.section.SectionApiRequester;
import nextstep.subway.acceptance.station.StationApiRequester;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.SectionCreateRequest;
import nextstep.utils.JsonPathUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
@AcceptanceTest
public class FavoriteAcceptanceTest {

    Long 교대역id;
    Long 강남역id;
    Long 양재역id;
    Long 남부터미널역id;
    Long 이호선id;
    Long 신분당선id;
    Long 삼호선id;

    @BeforeEach
    void setUp() {
        교대역id = JsonPathUtil.getId(StationApiRequester.createStationApiCall("교대역"));
        강남역id = JsonPathUtil.getId(StationApiRequester.createStationApiCall("강남역"));
        양재역id = JsonPathUtil.getId(StationApiRequester.createStationApiCall("양재역"));
        남부터미널역id = JsonPathUtil.getId(StationApiRequester.createStationApiCall("남부터미널역"));

        LineCreateRequest 이호선 = new LineCreateRequest("2호선", "green", 교대역id, 강남역id, 10);
        이호선id = JsonPathUtil.getId(LineApiRequester.createLineApiCall(이호선));

        LineCreateRequest 신분당선 = new LineCreateRequest("신분당선", "red", 강남역id, 양재역id, 10);
        신분당선id = JsonPathUtil.getId(LineApiRequester.createLineApiCall(신분당선));

        LineCreateRequest 삼호선 = new LineCreateRequest("3호선", "orange", 교대역id, 남부터미널역id, 2);
        삼호선id = JsonPathUtil.getId(LineApiRequester.createLineApiCall(삼호선));

        SectionCreateRequest 남부터미널양재역 = new SectionCreateRequest(남부터미널역id, 양재역id, 3);
        SectionApiRequester.generateSection(남부터미널양재역, 삼호선id);
    }

    /**
     * When 즐겨찾기를 등록하고
     * Then 즐겨찾기를 조회하면 즐겨찾기한 역의 경로가 조회된다
     */
    @DisplayName("즐겨찾기 등록")
    @Test
    void createFavorite() {
        //when
        FavoriteRequest request = new FavoriteRequest(교대역id, 양재역id);

        ExtractableResponse<Response> response = FavoriteApiRequester.createFavoriteApiCall(request);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> findResponse = FavoriteApiRequester.findFavoritesApiCall();

        assertThat(getFavorites(findResponse)).hasSize(1);
    }

    /**
     * When 경로를 찾을 수 없거나 연결되지 않은 경로를 즐겨찾기로 등록하면
     * Then 예외가 발생한다
     */
    @DisplayName("경로를 찾을 수 없거나 연결되지 않은 경로 즐겨찾기 등록")
    @Test
    void createDisconnectStationsFavorite() {
        //given
        Long 건대입구역id = JsonPathUtil.getId(StationApiRequester.createStationApiCall("건대입구역"));
        Long 성수역id = JsonPathUtil.getId(StationApiRequester.createStationApiCall("성수역"));

        //when
        FavoriteRequest request = new FavoriteRequest(건대입구역id, 성수역id);

        ExtractableResponse<Response> response = FavoriteApiRequester.createFavoriteApiCall(request);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.asPrettyString()).isEqualTo("연결되어있지 않은 출발역과 도착역의 경로는 조회할 수 없습니다.");
    }

    /**
     * Given 즐겨찾기를 등록하고
     * When 즐겨찾기를 조회하면
     * Then 즐겨찾기한 역의 경로가 조회된다
     */
    @DisplayName("즐겨찾기 경로 조회")
    @Test
    void findFavorites() {
        //given
        FavoriteRequest 교대양재역 = new FavoriteRequest(교대역id, 양재역id);
        FavoriteApiRequester.createFavoriteApiCall(교대양재역);

        FavoriteRequest 교대강남역 = new FavoriteRequest(교대역id, 강남역id);
        FavoriteApiRequester.createFavoriteApiCall(교대강남역);

        //when
        ExtractableResponse<Response> response = FavoriteApiRequester.findFavoritesApiCall();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getFavorites(response)).hasSize(2);
    }

    /**
     * Given 즐겨찾기를 등록하고
     * When 등록한 즐겨찾기를 삭제하면
     * Then 즐겨찾기가 삭제된다
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        //given
        FavoriteRequest request = new FavoriteRequest(교대역id, 양재역id);

        ExtractableResponse<Response> createResponse = FavoriteApiRequester.createFavoriteApiCall(request);

        Long id = Long.valueOf(createResponse.asString());

        //when
        ExtractableResponse<Response> response = FavoriteApiRequester.deleteFavoriteApiCall(id);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> findResponse = FavoriteApiRequester.findFavoritesApiCall();
        assertThat(getFavorites(findResponse)).hasSize(0);
    }

    private static List<FavoriteResponse> getFavorites(ExtractableResponse<Response> findResponse) {
        return JsonPathUtil.getList(findResponse, "favorites", FavoriteResponse.class);
    }
}
