package atdd.favorite.application.dto;

import atdd.favorite.domain.FavoritePath;
import atdd.path.domain.Station;

import java.util.List;

public class FavoritePathResponseView {
    private Long id;
    private String email;
    private List<Station> favoritePathStations;

    public FavoritePathResponseView() {
    }

    public FavoritePathResponseView(Long id, String email, List<Station> favoritePathStations) {
        this.id = id;
        this.email = email;
        this.favoritePathStations = favoritePathStations;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<Station> getFavoritePathStations() {
        return favoritePathStations;
    }

    public static FavoritePathResponseView of(FavoritePath favoritePath,
                                              List<Station> favoritePathStations){
        return new FavoritePathResponseView(
                favoritePath.getId(),
                favoritePath.getEmail(),
                favoritePathStations
        );
    }
}
