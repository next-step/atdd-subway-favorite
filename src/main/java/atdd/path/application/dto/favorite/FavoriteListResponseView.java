package atdd.path.application.dto.favorite;

import atdd.path.domain.Favorite;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class FavoriteListResponseView {
    private List<Favorite> favorites;

    @Builder
    public FavoriteListResponseView(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public static FavoriteListResponseView toDtoEntity(List<Favorite> favorites) {
        return FavoriteListResponseView.builder()
                .favorites(favorites)
                .build();
    }

    public Favorite getFirstIndex() {
        return this.favorites.get(0);
    }

    public Favorite getSecondIndex() {
        return this.favorites.get(1);
    }

    public int getSize() {
        return this.favorites.size();
    }
}
