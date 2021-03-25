package nextstep.subway.favorite.application;

import nextstep.subway.error.NotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
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

    public FavoriteResponse addFavorite(Long memberId, FavoriteRequest request) {
        Station source = stationService.findByStation(request.getSource());
        Station target = stationService.findByStation(request.getTarget());

        validateExists(memberId, source, target);

        Favorite favorite = favoriteRepository.save(new Favorite(memberId, source, target));

        return FavoriteResponse.of(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAllFavorites(Long memberId) {
        return favoriteRepository.findAllByMemberId(memberId)
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void removeFavorite(Long favoriteId) {
        Favorite favorite = findFavorite(favoriteId);

        favoriteRepository.delete(favorite);
    }

    private void validateExists(Long memberId, Station source, Station target) {
        boolean isExistsFavorite = favoriteRepository.findAllByMemberId(memberId).stream()
                .filter(f -> f.getSource().equals(source))
                .anyMatch(f -> f.getTarget().equals(target));

        if (isExistsFavorite) {
            throw new IllegalArgumentException("이미 존재하는 즐겨찾기 입니다.");
        }
    }

    private Favorite findFavorite(Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new NotFoundException(favoriteId));
    }
}
