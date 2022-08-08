package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(Long userId, Long source, Long target) {
        if (Objects.equals(source, target)) {
            throw new IllegalArgumentException("출발역과 도착역이 동일합니다");
        }

        var sourceStation = getStation(source);
        var targetStation = getStation(target);

        var favorite = favoriteRepository.save(Favorite.of(userId, sourceStation, targetStation));

        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> getFavorites(Long userId) {
        return favoriteRepository.findAllByUserId(userId)
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeFavorite(Long userId, Long favoriteId) {
        var favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(IllegalArgumentException::new);
        if (!Objects.equals(favorite.getUserId(), userId)) {
            throw new IllegalArgumentException();
        }
        favoriteRepository.deleteById(favoriteId);
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(IllegalArgumentException::new);
    }
}
