package nextstep.utils;

import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.line.application.dto.SectionRequest;
import nextstep.station.domain.Station;
import nextstep.station.application.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class UnitTestFixture {
    public static final String RED = "bg-red-600";
    public static final int DEFAULT_DISTANCE = 10;
    public static final int DISTANCE_4 = 4;
    public static final int DISTANCE_6 = 6;
    public static final int DISTANCE_7 = 7;
    public static final Station 강남역 = Station.of(1L, "강남역");
    public static final Station 양재역 = Station.of(2L, "양재역");
    public static final Station 교대역 = Station.of(3L, "교대역");
    public static final Station 홍대역 = Station.of(4L, "홍대역");
    public static final Line 신분당선 = 신분당선(강남역, 양재역);
    public static final Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);
    public static final Section 양재역_교대역 = createSection(신분당선, 양재역, 교대역, DISTANCE_4);
    public static final Section 교대역_홍대역 = createSection(신분당선, 교대역, 홍대역, DISTANCE_6);
    public static final Section 홍대역_강남역 = createSection(신분당선, 홍대역, 강남역, DISTANCE_7);
    public static final List<Section> 연결된구간 = List.of(강남역_양재역, 양재역_교대역, 교대역_홍대역, 홍대역_강남역);

    public static Line 신분당선(Station upStation, Station downStation) {
        return Line.builder()
                .id(1L)
                .name("2호선")
                .color("bg-red-600")
                .upStation(upStation)
                .downStation(downStation)
                .distance(DEFAULT_DISTANCE)
                .build();
    }

    public static Line 분당선(Station upStation, Station downStation) {
        return Line.builder()
                .name("분당선")
                .color(RED)
                .upStation(upStation)
                .downStation(downStation)
                .distance(DISTANCE_4)
                .build();
    }

    public static Line 중앙선(Station upStation, Station downStation) {
        return Line.builder()
                .name("중앙선")
                .color(RED)
                .upStation(upStation)
                .downStation(downStation)
                .distance(DISTANCE_6)
                .build();
    }

    public static Line 경의선(Station upStation, Station downStation) {
        return Line.builder()
                .name("경의선")
                .color(RED)
                .upStation(upStation)
                .downStation(downStation)
                .distance(DISTANCE_7)
                .build();
    }

    public static Section createSection(Line line, Station upStation, Station downStation, int distance) {
        return Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }

    public static SectionRequest createSectionRequest(Long upStationId, Long downStationId) {
        return new SectionRequest(upStationId, downStationId, DEFAULT_DISTANCE);
    }

    public static List<StationResponse> createStationResponse(Station ...station) {
        List<StationResponse> stationResponses = new ArrayList<>();
        for (Station s : station) {
            stationResponses.add(new StationResponse(s.getId(), s.getName()));
        }
        return stationResponses;
    }
}
