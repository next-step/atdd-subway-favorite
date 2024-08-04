package nextstep.line.fixture;

import nextstep.line.application.LineCommandService;
import nextstep.line.payload.AddSectionRequest;
import nextstep.line.payload.CreateLineRequest;
import nextstep.station.payload.StationRequest;
import nextstep.station.service.StationService;
import org.springframework.stereotype.Service;

@Service
public class LineFixtureGenerator {

    private final LineCommandService lineCommandService;
    private final StationService stationService;

    public LineFixtureGenerator(final LineCommandService lineCommandService, final StationService service) {
        this.lineCommandService = lineCommandService;
        this.stationService = service;
    }

    /**
     * 교대역    --- *2호선* (10) ---    강남역
     * |                                 |
     * *3호선* (2)                   *신분당선* (10)
     * |                                 |
     * 남부터미널역  --- *3호선* (3) ---   양재
     */

    public LineFixture createLineFixture() {
        Long 교대역 = stationService.saveStation(new StationRequest("교대역")).getId();
        Long 강남역 = stationService.saveStation(new StationRequest("강남역")).getId();
        Long 양재역 = stationService.saveStation(new StationRequest("양재역")).getId();
        Long 남부터미널역 = stationService.saveStation(new StationRequest("남부터미널역")).getId();
        Long 부산역 = stationService.saveStation(new StationRequest("부산역")).getId();
        Long 서면역 = stationService.saveStation(new StationRequest("서면역")).getId();
        Long 이호선 = lineCommandService.saveLine(new CreateLineRequest("2호선", "green", 교대역, 강남역, 10L)).getId();
        Long 신분당선 = lineCommandService.saveLine(new CreateLineRequest("신분당선", "red", 강남역, 양재역, 10L)).getId();
        Long 삼호선 = lineCommandService.saveLine(new CreateLineRequest("3호선", "orange", 교대역, 남부터미널역, 2L)).getId();
        Long 부산2호선 =  lineCommandService.saveLine(new CreateLineRequest("부산2호선", "green", 부산역, 서면역, 2L)).getId();
        return new LineFixture(교대역, 강남역, 양재역, 남부터미널역, 부산역, 서면역, 이호선, 신분당선, 삼호선,부산2호선);
    }


}
