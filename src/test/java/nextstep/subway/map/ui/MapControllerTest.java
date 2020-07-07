package nextstep.subway.map.ui;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class MapControllerTest {
    @Test
    void showLineDetail() {
        MapController controller = new MapController();
        ResponseEntity entity = controller.showLineDetail();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
