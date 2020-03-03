package atdd.path.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FavoriteRouteRequestView {
    private Long id;
    private Long sourceStationId;
    private Long targetStationId;
}
