package nextstep.subway.applicaion;

import nextstep.auth.member.LoginMember;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.FavoritesResponse;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest request) {
        return null;
    }

    public FavoritesResponse findFavorites(LoginMember loginMember) {
        return null;
    }

    public void deleteFavorite(LoginMember loginMember) {

    }
}
