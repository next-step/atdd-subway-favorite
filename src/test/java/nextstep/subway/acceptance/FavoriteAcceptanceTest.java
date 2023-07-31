package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {



    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavorite() {

        //given
        var 교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        var 강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
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


}
