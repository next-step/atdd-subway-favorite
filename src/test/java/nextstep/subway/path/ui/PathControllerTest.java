package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.path.dto.PathResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PathControllerTest {
    @Test
    void name() {
        PathService pathService = mock(PathService.class);
        PathController pathController = new PathController(pathService);
        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(new PathResponse());

        ResponseEntity<PathResponse> entity = pathController.findPath(1L, 2L, PathType.DISTANCE);

        assertThat(entity.getBody()).isNotNull();
    }
}
