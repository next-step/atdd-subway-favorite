package atdd.path.application.dto;

import atdd.path.domain.FavoritePath;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class FavoritePathResponseView {
    private long id;
    private List<PathResponseView> paths;

    public FavoritePathResponseView() {
    }

    private FavoritePathResponseView(long id, List<PathResponseView> paths) {
        this.id = id;
        this.paths = paths;
    }

    public static FavoritePathResponseView of(final FavoritePath favoritePath) {
        PathResponseView pathResponseView = new PathResponseView(favoritePath.getSourceStationId(),
                favoritePath.getTargetStationId(),
                Arrays.asList(favoritePath.getSourceStation(), favoritePath.getTargetStation()));

        return new FavoritePathResponseView(favoritePath.getId(), Arrays.asList(pathResponseView));
    }
}
