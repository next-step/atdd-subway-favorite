package atdd.path.application.dto.favorite;

import atdd.path.domain.Favorite;
import atdd.path.domain.Item;
import atdd.path.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class FavoriteCreateRequestView {
    private Item item;
    private String type;

    @Builder
    public FavoriteCreateRequestView(Item item, String type) {
        this.item = item;
        this.type = type;
    }

    public Favorite toEntity(User user) {
        return Favorite.builder()
                .user(user)
                .item(item)
                .build();
    }
}
