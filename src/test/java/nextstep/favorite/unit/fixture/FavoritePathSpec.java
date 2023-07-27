package nextstep.favorite.unit.fixture;

import nextstep.favorite.domain.FavoritePath;
import nextstep.member.domain.Member;

import static nextstep.utils.UnitTestUtils.createEntityTestId;

public class FavoritePathSpec {
    private FavoritePathSpec() {
    }

    public static FavoritePath of(Member member, Long sourceId, Long targetId) {
        final FavoritePath favoritePath = FavoritePath.builder()
                .member(member)
                .sourceId(sourceId)
                .targetId(targetId)
                .build();

        createEntityTestId(favoritePath, 1L);

        return favoritePath;
    }
}
