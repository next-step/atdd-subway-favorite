package nextstep.subway.favorite.service;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.CannotDeleteFavoriteException;
import nextstep.subway.favorite.exception.NoFavoriteToRemoveException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Station source = stationService.findStationById(favoriteRequest.getSourceId());
        Station target = stationService.findStationById(favoriteRequest.getTargetId());

        Favorite favorite = favoriteRepository.save(new Favorite(memberId, source, target));
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> getFavorites(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        Favorite favorite = getFavorite(favoriteId);

        if(!memberId.equals(favorite.getMemberId())) {
            throw new CannotDeleteFavoriteException();
        }

        favoriteRepository.deleteById(favoriteId);
    }

    private Favorite getFavorite(Long favoriteId) {
        return favoriteRepository.findById(favoriteId).orElseThrow(NoFavoriteToRemoveException::new);
    }
}
