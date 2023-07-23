package nextstep.subway.unit;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceMockTest {

    @Mock
    private SectionRepository sectionRepository;

    @Test
    void findPath() {
        //given
        String 출발역명 = "출발역";
        Long sourceId = 1L;
        String 도착역명 = "도착역";
        Long targetId = 2L;
        String 중간역명 = "중간역";
        Long middleId = 3L;

        Section 출발역_중간역 = new Section(new Line(), new Station(sourceId, 출발역명), new Station(middleId, 중간역명), 10);
        Section 중간역_도착역 = new Section(new Line(), new Station(middleId, 중간역명), new Station(targetId, 도착역명), 10);
        Section 출발역_도착역 = new Section(new Line(), new Station(sourceId, 출발역명), new Station(targetId, 도착역명), 19);
        when(sectionRepository.findAll()).thenReturn(List.of(출발역_중간역, 중간역_도착역, 출발역_도착역));
        PathService pathService = new PathService(sectionRepository);

        //when
        PathResponse response = pathService.find(sourceId, targetId);

        //then
        List<String> stationNames = response.getStations().stream().map(StationResponse::getName).collect(Collectors.toList());
        assertThat(stationNames).startsWith(출발역명).endsWith(도착역명);
        assertThat(response.getDistance()).isEqualTo(19);
    }
}
