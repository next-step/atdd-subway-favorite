package atdd.path.application.dto;

import java.util.List;

public class FavoritePathsResponseView {

    private List<FavoritePathResponseView> favorites;

    private FavoritePathsResponseView() {}

    public FavoritePathsResponseView(List<FavoritePathResponseView> favorites) {
        this.favorites = favorites;
    }

    public int getCount() {
        return favorites.size();
    }

    public List<FavoritePathResponseView> getFavorites() {
        return favorites;
    }

}
