package atdd.path.application.dto;

import java.util.List;

public class FavoritePathsResponseView {

    private int count;
    private List<FavoritePathResponseView> favorites;

    private FavoritePathsResponseView() {}

    public FavoritePathsResponseView(List<FavoritePathResponseView> favorites) {
        this.count = favorites.size();
        this.favorites = favorites;
    }

    public int getCount() {
        return count;
    }

    public List<FavoritePathResponseView> getFavorites() {
        return favorites;
    }

}
