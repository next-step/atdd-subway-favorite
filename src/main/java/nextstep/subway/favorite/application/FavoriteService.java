package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteStationService favoriteStationService;

    public FavoriteService(FavoriteRepository favoriteRepository, FavoriteStationService favoriteStationService) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteStationService = favoriteStationService;
    }

    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Favorite favorite = favoriteRepository.save(favoriteRequest.toFavorite(memberId));
        return favoriteStationService.convertToFavoriteResponse(favorite);
    }

    public List<FavoriteResponse> getFavorites(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
        return favoriteStationService.convertToFavoriteResponses(favorites);
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }
}
