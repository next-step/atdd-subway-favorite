package nextstep.subway.path.unit;

import nextstep.line.entity.Line;
import nextstep.path.dto.Path;
import nextstep.path.exception.PathException;
import nextstep.section.entity.Section;
import nextstep.section.entity.Sections;
import nextstep.station.dto.StationResponse;
import nextstep.station.entity.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static nextstep.common.constant.ErrorCode.PATH_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;

public class PathTest {

    Station 강남역;
    Station 역삼역;
    Section 강남역_역삼역_구간;
    Sections 구간들;
    Line 신분당선;
    double pathWeight = 10.0;
    Path path;

    @BeforeEach
    public void setup() {
        강남역 = Station.of(1L, "강남역");
        역삼역 = Station.of(2L, "역삼역");


        강남역_역삼역_구간 = Section.of(강남역, 역삼역, 10L);
        구간들 = new Sections(Collections.singletonList(강남역_역삼역_구간));
        신분당선 = Line.of(1L, "신분당선", "red", 15L, 구간들);
        path = Path.of(List.of(강남역, 역삼역), pathWeight);

    }

    @DisplayName("getVertexList와 getWeight의 정상 동작을 확인한다.")
    @Test
    public void getVertexList_getWeight() {
        // then
        assertAll(
                () -> assertEquals(List.of(강남역, 역삼역), path.getStations()),
                () -> assertEquals(pathWeight, path.getWeight())
        );
    }

    @DisplayName("[createPathResponse] pathResponse를 생성한다.")
    @Test
    void createPathResponse_success() {
        // when
        var pathResponse = path.createPathResponse();

        // then
        assertAll(
                () -> assertNotNull(pathResponse),
                () -> assertEquals(pathWeight, pathResponse.getDistance()),
                () -> assertEquals(List.of(
                        StationResponse.of(강남역.getId(), 강남역.getName()),
                        StationResponse.of(역삼역.getId(), 역삼역.getName())
                ), pathResponse.getStationResponses())
        );
    }

    @DisplayName("[createPathResponse] path의 stationList가 비어 있으면 예외가 발생한다.")
    @Test
    void createPathResponse_fail1() {
        // given
        var path = Path.of(List.of(), pathWeight);

        // when & then
        assertThrows(PathException.class, () -> path.createPathResponse())
                .getMessage().equals(PATH_NOT_FOUND.getDescription());
    }

}

