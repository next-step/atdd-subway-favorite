package atdd.path.application.dto.favorite;

import atdd.path.domain.Favorite;
import atdd.path.domain.Station;
import atdd.path.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class FavoriteCreateRequestView {
    private Long userId;
    private Long stationId;
    private String stationName;

    @Builder
    public FavoriteCreateRequestView(Long userId, Long stationId, String stationName) {
        this.userId = userId;
        this.stationId = stationId;
        this.stationName = stationName;
    }

    public Favorite toEntity(User user) {
        return Favorite.builder()
                .user(user)
                .station(new Station(stationId, stationName))
                .build();
    }
}
