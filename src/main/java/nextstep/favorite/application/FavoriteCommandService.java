package nextstep.favorite.application;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.payload.FavoriteRequest;
import nextstep.station.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class FavoriteCommandService {
    private final FavoriteRepository favoriteRepository;

    private final StationRepository stationRepository;

    public FavoriteCommandService(final FavoriteRepository favoriteRepository, final StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public Long createFavorite(final Long memberId, final FavoriteRequest request) {
        Favorite favorite = new Favorite(memberId, request.getSource(), request.getTarget());
        Favorite saved = favoriteRepository.save(favorite);
        return saved.getId();
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     *
     * @param id
     */
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
