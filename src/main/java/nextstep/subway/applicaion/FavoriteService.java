package nextstep.subway.applicaion;

import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    public FavoriteResponse saveFavorite(final FavoriteRequest request, final LoginMember loginMember) {
        return null;
    }

    public List<FavoriteResponse> findAllFavorites(final LoginMember loginMember) {
        return null;
    }

    public void deleteFavoriteById(final Long id, final LoginMember loginMember) {

    }

}
