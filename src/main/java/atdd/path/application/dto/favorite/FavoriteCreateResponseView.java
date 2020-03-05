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
public class FavoriteCreateResponseView {
    private Long id;
    private User user;
    private Item item;

    @Builder
    public FavoriteCreateResponseView(Long id, User user, Item item) {
        this.id = id;
        this.user = user;
        this.item = item;
    }

    public static FavoriteCreateResponseView toDtoEntity(Favorite favorite) {
        return FavoriteCreateResponseView.builder()
                .id(favorite.getId())
                .user(favorite.getUser())
                .item(favorite.getItem())
                .build();
    }
}
