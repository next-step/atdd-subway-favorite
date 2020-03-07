package atdd.path.application.dto;

import atdd.path.domain.FavoritePath;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class FavoritePathResponseView implements Serializable {
    private static final long serialVersionUID = -7756252127767730415L;

    private Long id;
    private PathResponseView path;

    public FavoritePathResponseView() {
    }

    public FavoritePathResponseView(Long id, PathResponseView path) {
        this.id = id;
        this.path = path;
    }

    public static FavoritePathResponseView of(final FavoritePath favoritePath) {
        PathResponseView pathResponseView = new PathResponseView(favoritePath.getSourceStation().getId(),
                favoritePath.getTargetStation().getId());

        return new FavoritePathResponseView(favoritePath.getId(), pathResponseView);
    }

    public static List<FavoritePathResponseView> listOf(final List<FavoritePath> favoritePath) {
        return favoritePath.stream().map(it ->
                new FavoritePathResponseView(it.getId(),
                        new PathResponseView(it.getSourceStation().getId(), it.getTargetStation().getId()))
        ).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public PathResponseView getPath() {
        return path;
    }
}
