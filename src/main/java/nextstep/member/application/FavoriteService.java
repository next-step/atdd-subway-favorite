package nextstep.member.application;

import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.RequestedStation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

  private final FavoriteRepository favoriteRepository;
  private final StationQueryService stationQueryService;

  public FavoriteService(FavoriteRepository favoriteRepository, StationQueryService stationQueryService) {
    this.favoriteRepository = favoriteRepository;
    this.stationQueryService = stationQueryService;
  }

  @Transactional
  public Long saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
    stationQueryService.searchStation(favoriteRequest.getSourceId());
    stationQueryService.searchStation(favoriteRequest.getTargetId());
    Favorite favorite = new Favorite(favoriteRequest.getSourceId(), favoriteRequest.getTargetId(), memberId);
    favoriteRepository.save(favorite);
    return favorite.getId();
  }

  public List<FavoriteResponse> searchFavorites(Long memberId) {
    return favoriteRepository.findByMemberId(memberId)
      .stream()
      .map(FavoriteResponse::of)
      .collect(Collectors.toList());
  }

  @Transactional
  public void deleteFavorite(Long id) {
    favoriteRepository.deleteById(id);
  }
}
