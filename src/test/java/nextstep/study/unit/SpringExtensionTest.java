package nextstep.study.unit;

import com.google.common.collect.Lists;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("단위 테스트 - SpringExtension을 활용한 가짜 협력 객체 사용")
@ExtendWith(SpringExtension.class)
public class SpringExtensionTest {
    @MockBean
    private LineRepository lineRepository;
    @MockBean
    private StationRepository stationRepository;

    @Test
    void findAllLines() {
        // given
        when(lineRepository.findAll()).thenReturn(Lists.newArrayList(new Line()));
        LineService lineService = new LineService(lineRepository, stationRepository);

        // when
        List<LineResponse> responses = lineService.findAllLines();

        // then
        assertThat(responses).hasSize(1);
    }
}
