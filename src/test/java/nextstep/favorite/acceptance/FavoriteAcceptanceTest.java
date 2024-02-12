package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.api.*;
import nextstep.core.AcceptanceTest;
import nextstep.core.AcceptanceTestAuthBase;
import nextstep.core.RestAssuredHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
@AcceptanceTest
public class FavoriteAcceptanceTest extends AcceptanceTestAuthBase {
    private final int 교대역_강남역_distance = 5;
    private final int 강남역_양재역_distance = 10;
    private final int 교대역_남부터미널_distance = 2;
    private final int 남부터미널_양재역_distance = 3;
    private Long 강남역_Id;
    private Long 교대역_Id;
    private Long 양재역_Id;
    private Long 남부터미널역_Id;
    private Long 사당역_Id;
    private Long 서울역_Id;
    private Long 이호선_Id;
    private Long 신분당선_Id;
    private Long 삼호선_Id;

    /**
     * Given 지하철 노선을 생성하고
     */
    @BeforeEach
    void setUp() {
        // given
        교대역_Id = RestAssuredHelper.getIdFromBody(StationApiHelper.createStation("교대역"));
        강남역_Id = RestAssuredHelper.getIdFromBody(StationApiHelper.createStation("강남역"));
        양재역_Id = RestAssuredHelper.getIdFromBody(StationApiHelper.createStation("양재역"));
        남부터미널역_Id = RestAssuredHelper.getIdFromBody(StationApiHelper.createStation("남부터미널역"));
        서울역_Id = RestAssuredHelper.getIdFromBody(StationApiHelper.createStation("서울역"));
        사당역_Id = RestAssuredHelper.getIdFromBody(StationApiHelper.createStation("사당역"));
        이호선_Id = RestAssuredHelper.getIdFromBody((LineApiHelper.createLine("2호선", "green", 교대역_Id, 강남역_Id, 교대역_강남역_distance)));
        신분당선_Id = RestAssuredHelper.getIdFromBody((LineApiHelper.createLine("신분당선", "red", 강남역_Id, 양재역_Id, 강남역_양재역_distance)));
        삼호선_Id = RestAssuredHelper.getIdFromBody((LineApiHelper.createLine("3호선", "orange", 교대역_Id, 남부터미널역_Id, 교대역_남부터미널_distance)));
        SectionApiHelper.createSection(삼호선_Id, 남부터미널역_Id, 양재역_Id, 남부터미널_양재역_distance);
    }


    /**
     * When 로그인 정보와 함께 출발역과 도착역을 통해 경로를 즐겨찾기에 추가 하면
     * Then 즐겨찾기 목록 조회 시 생성한 즐겨찾기를 찾을 수 있다
     */
    @DisplayName("출발역과 도착역을 통해 즐겨찾기를 추가 할 수 있다.")
    @Test
    void 즐겨찾기_추가_성공_테스트() {
        // when
        final ExtractableResponse<Response> response = 즐겨찾기_추가_요청_With_로그인(강남역_Id, 남부터미널역_Id);

        // then
        즐겨찾기_추가_요청이_성공한다(response);

        // then
        즐겨찾기_목록_조회_시_생성한_즐겨찾기를_찾을_수_있다(response);
    }

    private void 즐겨찾기_목록_조회_시_생성한_즐겨찾기를_찾을_수_있다(final ExtractableResponse<Response> response) {
        final Long createdId = RestAssuredHelper.getIdFromHeader(response);
        final ExtractableResponse<Response> listResponse = 즐겨찾기_목록_조회_요청_With_로그인();
        final List<Long> ids = listResponse.jsonPath().getList("id", Long.class);
        assertThat(ids).containsAnyOf(createdId);
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청_With_로그인() {
        return FavoriteApiHelper.fetchFavorites(accessToken);
    }

    private ExtractableResponse<Response> 즐겨찾기_추가_요청_With_로그인(final Long sourceId, final Long targetId) {
        return FavoriteApiHelper.addFavorite(accessToken, sourceId, targetId);
    }

    private void 즐겨찾기_추가_요청이_성공한다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

}
