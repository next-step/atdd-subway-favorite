package nextstep.line.unit;

import nextstep.line.domain.Line;
import nextstep.line.domain.LineRepository;
import nextstep.line.application.dto.LineResponse;
import nextstep.line.application.LineService;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import nextstep.utils.DatabaseCleanup;
import nextstep.utils.UnitTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.utils.UnitTestFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("테스트DB 사용한 지하철 노선 서비스 테스트")
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class LineServiceTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute(getClass());
    }

    @DisplayName("구간을 추가 함수는, 특정 노선에 구간을 추가하면 해당 구간이 추가된 노선 정보가 반환된다.")
    @Test
    void addSection() {
        // given
        Station 강남역 = stationRepository.save(UnitTestFixture.강남역);
        Station 양재역 = stationRepository.save(UnitTestFixture.양재역);
        Station 교대역 = stationRepository.save(UnitTestFixture.교대역);
        Line line = lineRepository.save(신분당선(강남역, 양재역));

        // when
        LineResponse lineResponse = lineService.addSections(line.getId(), createSectionRequest(양재역.getId(), 교대역.getId()));

        // then
        assertThat(lineResponse.getStations()).isEqualTo(createStationResponse(강남역, 양재역, 교대역));
    }
}
