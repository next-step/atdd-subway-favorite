package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.application.dto.FavoriteStationResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.subway.applicaion.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(final FavoriteRepository favoriteRepository, final StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    @Transactional
    public FavoriteResponse saveFavorite(final long memberId, final FavoriteRequest favoriteRequest) {
        final Favorite favorite = favoriteRequest.toFavorite(memberId);
        final Favorite savedFavorite = favoriteRepository.save(favorite);

        return createFavoriteResponse(savedFavorite);
    }

    private FavoriteResponse createFavoriteResponse(final Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                new FavoriteStationResponse(stationService.findById(favorite.getSource())),
                new FavoriteStationResponse(stationService.findById(favorite.getTarget())));
    }

}
