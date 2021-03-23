package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.IsExistFavoriteException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public List<FavoriteResponse> findAllOfMember(long memberId) {
        return findAllByMemberId(memberId)
                    .stream()
                    .map(FavoriteResponse::of)
                    .collect(Collectors.toList());
    }

    public Favorite addFavorite(long memberId, FavoriteRequest favoriteRequest) {
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());
        validateDuplication(memberId, source, target);

        return favoriteRepository.save(new Favorite(memberId, source, target));
    }

    private void validateDuplication(long memberId, Station source, Station target) {
        if (favoriteRepository.existsByMemberIdAndSourceAndTarget(memberId, source, target)) {
            throw new IsExistFavoriteException();
        }
    }

    public void removeFavorite(long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }

    private List<Favorite> findAllByMemberId(long memberId) {
        return favoriteRepository.findAllByMemberId(memberId);
    }
}
