package nextstep.subway.map.ui;

import nextstep.subway.line.application.LineService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MapControllerTest {
    @Test
    void showLineDetail() {
        LineService lineService = mock(LineService.class);
        MapController controller = new MapController(lineService);

        ResponseEntity entity = controller.showLineDetail();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(lineService).findAllLinesWithStations();
    }
}
