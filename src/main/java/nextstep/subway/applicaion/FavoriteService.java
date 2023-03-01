package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.exceptions.NotFoundFavoriteException;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    @Transactional
    public FavoriteResponse saveFavorite(Long memberId, Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationService.findById(sourceStationId);
        Station targetStation = stationService.findById(targetStationId);

        Favorite favorite = favoriteRepository.save(new Favorite(memberId, sourceStation, targetStation));
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findByMember(long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long favoriteId, Long memberId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new NotFoundFavoriteException("존재하지 않는 즐겨찾기 아이디 입니다. favoriteId: " + favoriteId));

        favorite.delete(memberId);

        favoriteRepository.delete(favorite);
    }

}
