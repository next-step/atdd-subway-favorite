package nextstep.subway.applicaion;

import nextstep.auth.secured.RoleAuthenticationException;
import nextstep.subway.applicaion.dto.FavoriteResponse;
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
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    @Transactional
    public Long createFavorite(Long memberId, Long sourceId, Long targetId) {
        if (favoriteRepository.existsBySourceIdAndTargetId(sourceId, targetId)) {
            throw new IllegalArgumentException("이미 존재하는 즐겨찾기입니다.");
        }
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);
        Favorite favorite = favoriteRepository.save(new Favorite(memberId, source, target));
        return favorite.getId();
    }

    @Transactional
    public void deleteFavorite(Long memberId, Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (!favorite.equalMember(memberId)) {
            throw new RoleAuthenticationException("권한이 없습니다.");
        }
        favoriteRepository.delete(favorite);
    }

    public List<FavoriteResponse> findFavorites(Long id) {
        return favoriteRepository.findAllByMemberId(id).stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }
}
