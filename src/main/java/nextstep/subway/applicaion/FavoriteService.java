package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.member.domain.MemberRepository;
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

  public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
    this.favoriteRepository = favoriteRepository;
    this.stationRepository = stationRepository;
  }

  @Transactional
  public FavoriteResponse saveFavorite(Long userId, FavoriteRequest request) {

    Station source = stationRepository.findById(request.getSource())
        .orElseThrow(() -> new IllegalArgumentException("출발역이 존재 하지 않습니다."));
    Station target = stationRepository.findById(request.getTarget())
        .orElseThrow(() -> new IllegalArgumentException("마지막역이 존재 하지 않습니다."));

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
  public void deleteLine(Long id) {
    favoriteRepository.deleteById(id);
  }
}
