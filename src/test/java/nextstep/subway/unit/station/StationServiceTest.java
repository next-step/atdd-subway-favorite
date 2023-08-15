package nextstep.subway.unit.station;

import nextstep.subway.application.StationService;
import nextstep.subway.application.dto.station.StationRequest;
import nextstep.subway.application.dto.station.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class StationServiceTest {

    @Autowired
    StationService stationService;

    @DisplayName("지하철 역을 생성한다.")
    @Test
    void createStation() {
        // given
        StationRequest 교대역_요청 = new StationRequest("교대역");

        // when
        StationResponse 교대역 = stationService.createStation(교대역_요청);

        // then
        Assertions.assertThat(교대역.getId()).isEqualTo(1L);
    }
}