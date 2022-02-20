package nextstep.subway.domain.favourite;

import nextstep.subway.domain.member.Member;
import nextstep.utils.exception.FavouriteNotMineException;

public class FavouriteValidator {
    public static void validateAcceptable(Favourite targetFavourite, Member member) {
        if (targetFavourite.isAcceptable(member)) {
            return;
        }
        throw new FavouriteNotMineException();
    }
}
