package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberFavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public MemberFavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public void createFavorite(Long memberId, FavoriteRequest request) {
        //TODO: 회원별 즐겨찾기 생성 로직 구현
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        //TODO: 회원별 즐겨찾기 삭제 로직 구현 / 권한체크 필요
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        //TODO: 본인의 즐겨찾기 리스트 조회 구현
        return null;
    }
}
