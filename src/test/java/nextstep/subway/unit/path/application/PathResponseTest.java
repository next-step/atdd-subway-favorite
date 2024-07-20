package nextstep.subway.unit.path.application;

import static nextstep.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import nextstep.subway.path.application.dto.PathResponse;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.application.dto.StationResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class PathResponseTest {
  @DisplayName("경로로부터 응답을 생성한다.")
  @Test
  void from() {
    Station 교대역 = 교대역();
    Station 남부터미널역 = 남부터미널역();
    Station 양재역 = 양재역();
    Path path = Path.of(Arrays.asList(교대역, 남부터미널역, 양재역), 5);

    PathResponse response = PathResponse.from(path);

    assertThat(response.getStations())
        .containsExactly(
            StationResponse.from(교대역), StationResponse.from(남부터미널역), StationResponse.from(양재역));
    assertThat(response.getDistance()).isEqualTo(5);
  }
}
