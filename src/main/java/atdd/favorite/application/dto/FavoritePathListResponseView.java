package atdd.favorite.application.dto;

import atdd.favorite.domain.FavoritePath;

import java.util.List;

public class FavoritePathListResponseView {
    private String userEmail;
    private List<FavoritePath> favoritePaths;

    public FavoritePathListResponseView(String userEmail, List<FavoritePath> favoritePaths) {
        this.userEmail = userEmail;
        this.favoritePaths = favoritePaths;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public List<FavoritePath> getFavoritePaths() {
        return favoritePaths;
    }
}
