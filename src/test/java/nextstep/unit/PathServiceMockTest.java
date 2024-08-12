package nextstep.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.path.application.PathService;
import nextstep.path.domain.PathFinder;
import nextstep.station.domain.Station;
import nextstep.unit.fixture.SectionInMemoryRepository;
import nextstep.unit.fixture.StationInMemoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
public class PathServiceMockTest extends SectionInMemoryRepository {

    private PathService pathService;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @Test
    void getPath() {
        // given
        pathService = new PathService(new PathFinder(), new StationInMemoryRepository(), new SectionInMemoryRepository());

        // when
        List<Station> result = pathService.getPath(교대역_id, 양재역_id);

        // then
        assertThat(result).containsExactly(교대역, 남부터미널역, 양재역);
    }

    @Test
    void getPathWeight() {
        // given
        pathService = new PathService(new PathFinder(), new StationInMemoryRepository(), new SectionInMemoryRepository());

        // when
        double weight = pathService.getPathWeight(교대역_id, 양재역_id);

        // then
        assertThat(weight).isEqualTo(5);
    }
}
