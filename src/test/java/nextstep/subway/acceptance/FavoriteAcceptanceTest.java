package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청_후_id_추출;
import static org.assertj.core.api.Assertions.assertThat;

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
            "upStationId", 교대역+"",
            "downStationId", 강남역+"",
            "distance", 10+""
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
            "upStationId", 교대역+"",
            "downStationId", 강남역+"",
            "distance", 10+""
        ));

        지하철_노선에_지하철_구간_생성_요청(이호선, Map.of(
            "upStationId", 홍대역+"",
            "downStationId", 구디역+"",
            "distance", 10+""
        ));

        //when
        var response = 즐겨찾기_생성_요청(교대역, 홍대역, getAccessToken());

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
