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
    private Long itemId;
    private String itemName;
    private String type;

    @Builder
    public FavoriteCreateRequestView(Long itemId, String itemName, String type) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.type = type;
    }

    public Favorite toEntity(User user) {
        return Favorite.builder()
                .user(user)
                .item(new Station(itemId, itemName))
                .build();
    }
}
