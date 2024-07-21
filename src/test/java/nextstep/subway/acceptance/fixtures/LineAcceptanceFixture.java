package nextstep.subway.acceptance.fixtures;

import nextstep.subway.controller.dto.CreateLineRequest;

public class LineAcceptanceFixture {
    public static CreateLineRequest 일호선_생성_요청값을_생성한다(Long upStationId, Long downStationId) {
        return new CreateLineRequest("1호선", "#0052A4", upStationId, downStationId, 10L);
    }

    public static CreateLineRequest 이호선_생성_요청값을_생성한다(Long upStationId, Long downStationId) {
        return new CreateLineRequest("2호선", "#00A84D", upStationId, downStationId, 12L);
    }
}
