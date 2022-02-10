package nextstep.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.dto.FavoriteRequest;
import nextstep.favorite.dto.FavoriteResponse;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository,
        StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse saveFavorite(FavoriteRequest request) {
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());

        Favorite persistFavorite = favoriteRepository.save(Favorite.of(source.getId(), target.getId()));

        return FavoriteResponse.of(persistFavorite.getId(), source, target);
    }

    public List<FavoriteResponse> findAllFavorite() {
        return favoriteRepository.findAll().stream()
            .map(favorite -> FavoriteResponse.of(favorite.getId(),
                stationService.findById(favorite.getSource()),
                stationService.findById(favorite.getTarget())))
            .collect(Collectors.toList());
    }

    public void deleteById(Long id) {

    }
}
