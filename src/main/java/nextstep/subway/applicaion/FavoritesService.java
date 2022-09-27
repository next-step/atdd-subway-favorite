package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.FavoriteCreateRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoritesRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FavoritesService {
    private final FavoritesRepository favoritesRepository;
    private final StationService stationService;

    public FavoriteResponse createFavorite(final FavoriteCreateRequest request) {
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());

        Favorite favorite = new Favorite(source, target);
        favoritesRepository.save(favorite);

        return new FavoriteResponse(favorite);
    }
}
