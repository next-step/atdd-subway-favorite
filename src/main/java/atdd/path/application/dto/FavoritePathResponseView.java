package atdd.path.application.dto;

import atdd.path.domain.FavoritePath;
import atdd.path.domain.Station;

import java.util.List;

public class FavoritePathResponseView {

    private Long id;
    private PathResponseView path;

    private FavoritePathResponseView() { }

    public FavoritePathResponseView(FavoritePath favoritePath, List<Station> stations) {
        this.id = favoritePath.getId();
        this.path = new PathResponseView(
                favoritePath.getSourceStationId(),
                favoritePath.getTargetStationId(),
                stations);
    }

    public Long getId() {
        return id;
    }

    public PathResponseView getPath() {
        return path;
    }

}
