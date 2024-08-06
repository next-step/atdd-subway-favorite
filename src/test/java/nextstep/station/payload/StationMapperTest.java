package nextstep.station.payload;

import nextstep.station.domain.Station;
import nextstep.station.repository.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StationMapperTest {

    private StationRepository stationRepository;

    private StationMapper stationMapper;
    private List<Station> 전체역;

    @BeforeEach
    void setUp() {
        전체역 = List.of(
                new Station(1L, "교대역"),
                new Station(2L, "강남역"),
                new Station(3L, "양재역")
        );

        stationRepository = mock(StationRepository.class);
        stationMapper = new StationMapper(stationRepository);


    }

    @DisplayName("station을 한번에 조회해 pk값으로 StationResponse 찾을 수 있다.")
    @Test
    void mappingTest() {
        //given 데이터가 1L ,2L,3L 있을때
        when(stationRepository.findByIdIn(anyCollection()))
                .thenAnswer(invocation -> {
                    Collection<Long> ids = invocation.getArgument(0);
                    return ids.stream()
                            .map(this::findStationById)
                            .collect(Collectors.toList());
                });
        //when 1L, 2L 조회시
        var stationMap = stationMapper.getStationResponseMap(List.of(1L, 2L));

        //then 1L ,2L 은 반환되고 3L은 반환되지 않는다
        assertAll(() -> assertThat(stationMap.get(1L).getName()).isEqualTo("교대역"),
                () -> assertThat(stationMap.get(2L).getName()).isEqualTo("강남역"),
                () -> assertThat(stationMap.get(3L)).isNull()
        );
    }

    private Station findStationById(final Long id) {
        return 전체역.stream().filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }
}
