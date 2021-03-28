package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public List<FavoriteResponse> findFavoritesByMemberId(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAll();
        return favorites.stream()
                 .filter(it -> it.getMemberId().equals(memberId))
                 .map(FavoriteResponse::of)
                 .collect(Collectors.toList());
    }

    public FavoriteResponse saveFavorite(FavoriteRequest request, Long memberId) {
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());
        Favorite persistFavorite = favoriteRepository.save(new Favorite(source, target, memberId));
        return FavoriteResponse.of(persistFavorite);
    }

    public void removeFavorite(Long memberId, Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(NoSuchElementException::new);
        if (!favorite.isOwnedBy(memberId)) {
            throw new UnauthorizedFavoriteAccessException();
        }
        favoriteRepository.deleteById(id);
    }


}
