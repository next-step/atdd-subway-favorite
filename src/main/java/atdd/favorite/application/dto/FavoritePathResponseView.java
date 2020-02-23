package atdd.favorite.application.dto;

import atdd.path.domain.Station;

import java.util.List;


public class FavoritePathResponseView {
    private Long id;
    private String userEmail;
    private List<Station> favoritePath;

    public FavoritePathResponseView(Long id, String userEmail, List<Station> favoritePath) {
        this.id = id;
        this.userEmail = userEmail;
        this.favoritePath=favoritePath;
    }

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public List<Station> getFavoritePath() {
        return favoritePath;
    }
}
