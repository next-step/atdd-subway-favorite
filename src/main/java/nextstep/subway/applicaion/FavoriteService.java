package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.auth.AuthenticationException;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {

  private FavoriteRepository favoriteRepository;
  private StationRepository stationRepository;
  private PathService pathService;

  public FavoriteService(FavoriteRepository favoriteRepository,
      StationRepository stationRepository, PathService pathService) {
    this.favoriteRepository = favoriteRepository;
    this.stationRepository = stationRepository;
    this.pathService = pathService;
  }

  @Transactional
  public FavoriteResponse saveFavorite(Long userId, FavoriteRequest request) {

    Station source = stationRepository.findById(request.getSource())
        .orElseThrow(() -> new IllegalArgumentException("출발역이 존재 하지 않습니다."));
    Station target = stationRepository.findById(request.getTarget())
        .orElseThrow(() -> new IllegalArgumentException("마지막역이 존재 하지 않습니다."));

    pathService.findPath(request.getSource(), request.getTarget());
    Favorite favorite = favoriteRepository.save(Favorite.of(userId, source, target));
    return FavoriteResponse.of(favorite);
  }

  public List<FavoriteResponse> findFavorite(Long userId) {
    return favoriteRepository.findAllByMemberId(userId).stream()
        .map(favorite -> FavoriteResponse.of(favorite))
        .collect(
            Collectors.toList());
  }

  @Transactional
  public void deleteFavorite(Long userId, Long id) {
    Favorite deletedFavorite = favoriteRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 역은 존재 하지 않습니다."));
    if (!deletedFavorite.getMemberId().equals(userId)) {
      throw new AuthenticationException("내 즐겨찾기 목록에 있지 않습니다.");
    }

    favoriteRepository.deleteById(id);
  }
}
