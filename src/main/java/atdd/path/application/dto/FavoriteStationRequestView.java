package atdd.path.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FavoriteStationRequestView {
    private Long id;
    private Long stationId;
}
