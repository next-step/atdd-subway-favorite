package nextstep.common.fixture;

import nextstep.favorite.domain.Favorite;
import nextstep.station.domain.Station;
import nextstep.utils.ReflectionUtils;

public class FavoriteFactory {
    private FavoriteFactory() {

    }

    public static Favorite createFavorite(final Long memberId, final Station sourceStation, final Station targetStation) {
        return new Favorite(memberId, sourceStation, targetStation);
    }

    public static Favorite createFavorite(final Long id, final Long memberId, final Station sourceStation, final Station targetStation) {
        final Favorite favorite = createFavorite(memberId, sourceStation, targetStation);
        ReflectionUtils.injectIdField(favorite, id);
        return favorite;
    }

}
