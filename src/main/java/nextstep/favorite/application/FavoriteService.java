package nextstep.favorite.application;

import lombok.RequiredArgsConstructor;
import nextstep.favorite.presentation.FavoriteRequest;
import nextstep.favorite.presentation.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.infrastructure.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    public Long createFavorite(FavoriteRequest request, LoginMember loginMember) {
        // TODO 존재하는 역에 대한 검증

        Favorite createdFavorite = favoriteRepository.save(request.toFavorite(loginMember.getId()));
        return createdFavorite.getId();
    }

    /**
     * TODO: StationResponse 를 응답하는 FavoriteResponse 로 변환해야 합니다.
     *
     * @return
     */
    public List<FavoriteResponse> findFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();
        return null;
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     * @param id
     */
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
