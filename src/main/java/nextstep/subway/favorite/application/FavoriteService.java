package nextstep.subway.favorite.application;

import nextstep.subway.auth.exception.UnauthorizedException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());

        Favorite favorite = favoriteRepository.save(new Favorite(memberId, source, target));
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> getFavorites(Long memberId) {
        return favoriteRepository.findAll().stream()
                .filter(it -> it.isOwner(memberId))
                .map(it -> FavoriteResponse.of(it))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        Optional<Favorite> favoriteOptional = favoriteRepository.findById(favoriteId);

        if (!favoriteOptional.isPresent()) {
            throw new RuntimeException();
        }

        Favorite favorite = favoriteOptional.get();
        if (!favorite.isOwner(memberId)) {
            throw new UnauthorizedException();
        }

        favoriteRepository.deleteById(favoriteId);
    }
}
