package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.FavoriteCreateRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;


    public Favorite save(FavoriteCreateRequest request) {
        Favorite favorite = request.toEntity(stationService::findById);
        return favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> findByMemberId(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(memberId);
        return favorites.stream().map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteMyFavoriteById(Long favoriteId, Long memberId) {
        validateFavoriteForDelete(favoriteId, memberId);

        favoriteRepository.deleteById(favoriteId);
    }

    private void validateFavoriteForDelete(Long favoriteId, Long memberId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 즐겨찾기가 없습니다. id:" + favoriteId));

        if (!favorite.isMyFavorite(memberId)) {
            throw new IllegalArgumentException("자신의 즐겨찾기만 삭제 할 수 있습니다.");
        }
    }
}
