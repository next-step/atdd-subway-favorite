package nextstep.subway.station.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class StationRepositoryTest {
    @Autowired
    private StationRepository stationRepository;
    private Long 사용자 = 1L;

    @Test
    void saveStation() {
        stationRepository.save(new Station(사용자, "역이름"));
        assertThatThrownBy(() -> {
            stationRepository.save(new Station(사용자, "역이름"));
        }).isInstanceOf(RuntimeException.class);
    }
}
