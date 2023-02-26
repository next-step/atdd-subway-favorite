package nextstep.favorite.ui.response;

import java.util.Collections;
import java.util.List;
import nextstep.favorite.domain.Favorites;

public class FavoriteResponses {

    private List<FavoriteResponse> favorites;

    public FavoriteResponses() {
    }

    public FavoriteResponses(List<FavoriteResponse> favorites) {
        this.favorites = favorites;
    }

    public static FavoriteResponses from(Favorites favorites) {
        return new FavoriteResponses(
            favorites.getFavorites().stream()
                .map(FavoriteResponse::from)
                .collect(java.util.stream.Collectors.toList())
        );
    }

    public List<FavoriteResponse> getFavorites() {
        return Collections.unmodifiableList(favorites);
    }
}
