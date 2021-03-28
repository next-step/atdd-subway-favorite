package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.FavoriteAlreadyExistException;
import nextstep.subway.favorite.exception.InvalidFavoriteMemberException;
import nextstep.subway.member.exception.FavoriteNonExistException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService, FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public Long addFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Station source = stationService.findStationById(favoriteRequest.getSourceId());
        Station target = stationService.findStationById(favoriteRequest.getTargetId());

        validateExistFavorite(memberId, source, target);

        Favorite favorite = new Favorite(memberId, source, target);
        Favorite savedFavorite = favoriteRepository.save(favorite);

        return savedFavorite.getId();
    }

    private void validateExistFavorite(Long memberId, Station source, Station target) {
        if (favoriteRepository.existsByMemberIdAndSourceAndTarget(memberId, source, target)) {
            throw new FavoriteAlreadyExistException();
        }
    }

    public void removeFavorite(Long favoriteId, Long memberId) {
        Favorite favorite = findFavoriteById(favoriteId);
        if (!favorite.validateFavoriteOfMine(memberId)) {
             throw new InvalidFavoriteMemberException();
        }
        favoriteRepository.deleteById(favoriteId);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAllFavoriteResponsesByMemberId(Long memberId) {
        return findAllFavoritesByMemberId(memberId)
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Favorite> findAllFavoritesByMemberId(Long memberId) {
        return favoriteRepository.findAllByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public Favorite findFavoriteById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
                .orElseThrow(FavoriteNonExistException::new);
    }
}
