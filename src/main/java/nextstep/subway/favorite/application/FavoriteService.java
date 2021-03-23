package nextstep.subway.favorite.application;

import nextstep.subway.auth.dto.UserPrincipal;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.exception.NoAuthorizedException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private StationService stationService;
    private FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService, FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public Favorite saveFavorite(final UserPrincipal principal, final FavoriteRequest request){
        final Station source = stationService.findStationById(Long.parseLong(request.getSource()));
        final Station target = stationService.findStationById(Long.parseLong(request.getTarget()));
        return favoriteRepository.save(new Favorite(principal.getId(), source.getId(), target.getId()));
    }

    public List<FavoriteResponse> getFavorites(final UserPrincipal principal) {
        return favoriteRepository.findByUserId(principal.getId())
                .stream()
                .map(favorite -> convertFavorite(favorite))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(final UserPrincipal principal, Long id) {
        final Favorite favorite = favoriteRepository.findByIdAndUserId(id, principal.getId()).orElseThrow(RuntimeException::new);;
        favoriteRepository.deleteById(favorite.getId());
    }

    private FavoriteResponse convertFavorite(final Favorite favorite){
        return FavoriteResponse.of(favorite, stationService.findStationById(favorite.getSourceId()), stationService.findStationById(favorite.getTargetId()));
    }
}
