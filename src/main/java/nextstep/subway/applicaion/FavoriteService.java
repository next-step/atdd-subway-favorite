package nextstep.subway.applicaion;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final PathService pathService;


    public List<Favorite> getFavorites(Long memberId) {
        return favoriteRepository.findAllByMemberId(memberId);
    }

    public Favorite getFavorite(Long id) {
        return favoriteRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public Favorite createFavorite(Long memberId, FavoriteRequest request) {
        pathService.findPath(request.getSource(), request.getTarget());

        System.out.println("[FavoriteService createFavorite] " + TransactionSynchronizationManager.getCurrentTransactionName());
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());
        return favoriteRepository.save(new Favorite(source, target, memberId));
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
