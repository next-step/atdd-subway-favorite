package atdd.favorite.application.dto;

import atdd.favorite.domain.FavoritePath;

import java.util.List;

public class FavoritePathListResponseView {
    private String email;
    private List<FavoritePath> favoritePaths;

    public FavoritePathListResponseView() {
    }

    public FavoritePathListResponseView(String email) {
        this.email = email;
    }

    public FavoritePathListResponseView(String email, List<FavoritePath> favoritePaths) {
        this.email = email;
        this.favoritePaths = favoritePaths;
    }

    public String getEmail() {
        return email;
    }

    public List<FavoritePath> getFavoritePaths() {
        return favoritePaths;
    }
}
