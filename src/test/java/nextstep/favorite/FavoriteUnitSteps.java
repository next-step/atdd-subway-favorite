package nextstep.favorite;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.domain.Favorite;

public final class FavoriteUnitSteps {

    private FavoriteUnitSteps() {
        throw new IllegalStateException();
    }

    public static final String email = "test@email.com";
    public static final long memberId = 1L;
    public static final long source = 2L;
    public static final long target = 3L;

    public static FavoriteRequest favoriteRequest() {
        return new FavoriteRequest(source, target);
    }

    public static Favorite favorite() {
        return new Favorite(memberId, source, target);
    }
}
