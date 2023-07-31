package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_조회_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청_후_id_추출;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    @DisplayName("즐겨찾기 생성 (성공)")
    @Test
    void createFavorite() {

        //given
        var 교대역 = 지하철역_생성_요청_후_id_추출("교대역");
        var 강남역 = 지하철역_생성_요청_후_id_추출("강남역");
        var 이호선 = 지하철_노선_생성_요청("2호선", "green").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(이호선, Map.of(
            "upStationId", 교대역 + "",
            "downStationId", 강남역 + "",
            "distance", 10 + ""
        ));

        //when
        var response = 즐겨찾기_생성_요청(교대역, 강남역, getAccessToken());

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("즐겨찾기 생성 (실패) - 두 역사이의 경로가 연결되어있지 않음")
    @Test
    void createFavorite_whenDontConnectedPath() {

        //given
        var 교대역 = 지하철역_생성_요청_후_id_추출("교대역");
        var 강남역 = 지하철역_생성_요청_후_id_추출("강남역");

        var 홍대역 = 지하철역_생성_요청_후_id_추출("홍대역");
        var 구디역 = 지하철역_생성_요청_후_id_추출("구디역");

        var 이호선 = 지하철_노선_생성_요청("2호선", "green").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(이호선, Map.of(
            "upStationId", 교대역 + "",
            "downStationId", 강남역 + "",
            "distance", 10 + ""
        ));

        지하철_노선에_지하철_구간_생성_요청(이호선, Map.of(
            "upStationId", 홍대역 + "",
            "downStationId", 구디역 + "",
            "distance", 10 + ""
        ));

        //when
        var response = 즐겨찾기_생성_요청(교대역, 홍대역, getAccessToken());

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    void getFavorites() {

        //given
        final String userToken = getAccessToken();

        var 교대역 = 지하철역_생성_요청_후_id_추출("교대역");
        var 강남역 = 지하철역_생성_요청_후_id_추출("강남역");
        var 이호선 = 지하철_노선_생성_요청("2호선", "green").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(이호선, Map.of(
            "upStationId", 교대역 + "",
            "downStationId", 강남역 + "",
            "distance", 10 + ""
        ));

        즐겨찾기_생성_요청(교대역, 강남역, getAccessToken());

        //when
        var response = 즐겨찾기_조회_요청(userToken);

        //then
        assertAll(
            () -> assertThat(response.jsonPath().getLong("[0].source.id")).isEqualTo(교대역),
            () -> assertThat(response.jsonPath().getLong("[0].target.id")).isEqualTo(강남역)
        );

    }

    @DisplayName("즐겨찾기 삭제(성공)")
    @Test
    void deleteFavorites() {

        //given
        final String userToken = getAccessToken();

        var 교대역 = 지하철역_생성_요청_후_id_추출("교대역");
        var 강남역 = 지하철역_생성_요청_후_id_추출("강남역");
        var 이호선 = 지하철_노선_생성_요청("2호선", "green").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(이호선, Map.of(
            "upStationId", 교대역 + "",
            "downStationId", 강남역 + "",
            "distance", 10 + ""
        ));

        var createResponse = 즐겨찾기_생성_요청(교대역, 강남역, getAccessToken());

        //when
        var deleteResponse = 즐겨찾기_삭제_요청(createResponse, userToken);

        //then
        var response = 즐겨찾기_조회_요청(userToken);

        assertAll(
            () -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
            () -> assertThat(response.jsonPath().getList("")).isEmpty()
        );

    }

    @DisplayName("즐겨찾기 삭제(실패) - 내 즐겨찾기가 아닌경우 권한 인증 실패")
    @Test
    void deleteFavorites_failAuth() {

        //given
        final String userToken = getAccessToken();

        var 교대역 = 지하철역_생성_요청_후_id_추출("교대역");
        var 강남역 = 지하철역_생성_요청_후_id_추출("강남역");
        var 이호선 = 지하철_노선_생성_요청("2호선", "green").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(이호선, Map.of(
            "upStationId", 교대역 + "",
            "downStationId", 강남역 + "",
            "distance", 10 + ""
        ));

        var createResponse = 즐겨찾기_생성_요청(교대역, 강남역, getAccessToken());

        //when
        var deleteResponse = 즐겨찾기_삭제_요청(createResponse, "-1");

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }

}
