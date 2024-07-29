package nextstep.favorite.domain;

import java.util.List;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.domain.LoginMember;

public interface FavoriteService {
    Favorite createFavorite(FavoriteRequest request, LoginMember loginMember);

    List<FavoriteResponse> findFavorites(LoginMember loginMember);

    void deleteFavorite(Long id, LoginMember loginMember);
}
