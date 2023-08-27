package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;

import java.util.HashMap;
import java.util.Map;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

@DisplayName("즐겨찾기 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 교대역;
    private Long 양재역;

    @BeforeEach
    public void setUp() {
        교대역 = 지하철역_생성_요청("교대역").as(StationResponse.class).getId();
        양재역 = 지하철역_생성_요청("양재역").as(StationResponse.class).getId();

        지하철_노선_생성_요청(노선_생성_요청_파라미터_생성(교대역, 양재역));
    }

    private Map<String, String> 노선_생성_요청_파라미터_생성(Long upStationId, Long downStationId) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", "신분당선");
        lineCreateParams.put("color", "bg-orange-600");
        lineCreateParams.put("upStationId", upStationId + "");
        lineCreateParams.put("downStationId", downStationId + "");
        lineCreateParams.put("distance", 10 + "");
        return lineCreateParams;
    }

}
