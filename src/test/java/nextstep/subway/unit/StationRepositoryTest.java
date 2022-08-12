package nextstep.subway.unit;

import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.domain.SubwayErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class StationRepositoryTest {

    @Autowired
    private StationRepository stationRepository;

    private Station 강남역;
    private Station 병점역;
    private Station 선릉역;

    @BeforeEach
    void setUp() {
        강남역 = stationRepository.save(new Station("강남역"));
        병점역 = stationRepository.save(new Station("병점역"));
        선릉역 = stationRepository.save(new Station("선릉역"));
    }

    @Test
    void findAllStationByIds() {
        //given
        Set<Long> 등록된_역_ID_목록 = getStationIds(강남역.getId(), 병점역.getId(), 선릉역.getId());

        //when
        List<Station> findStations = stationRepository.findAllById(등록된_역_ID_목록);

        //then
        assertThat(
                findStations.stream().map(Station::getName).collect(Collectors.toList())
        ).contains("강남역", "병점역", "선릉역");
    }

    @Test
    void findAllStationByNotExistsIds() {
        //given
        Set<Long> 등록되지_않은_ID_목록 = getStationIds(99L, 88L, 77L);

        //then
        assertThatThrownBy(() -> stationRepository.findAllById(등록되지_않은_ID_목록))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(SubwayErrorMessage.NOT_FOUND_STATION.getMessage());
    }

    private Set<Long> getStationIds(Long ...ids) {
        return new HashSet<>(Arrays.asList(ids));
    }
}
