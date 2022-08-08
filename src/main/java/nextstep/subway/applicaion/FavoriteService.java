package nextstep.subway.applicaion;

import static java.util.stream.Collectors.toList;

import java.util.List;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.common.exception.CustomException;
import nextstep.subway.common.exception.StationErrorMessage;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

  private final MemberService memberService;
  private final StationService stationService;
  private final FavoriteRepository favoriteRepository;

  public FavoriteService(MemberService memberService, StationService stationService, FavoriteRepository favoriteRepository) {
    this.memberService = memberService;
    this.stationService = stationService;
    this.favoriteRepository = favoriteRepository;
  }

  @Transactional
  public FavoriteResponse saveFavorite(FavoriteRequest favoriteRequest, String email) {
    Station sourceStation = stationService.findById(favoriteRequest.getSource());
    Station targetStation = stationService.findById(favoriteRequest.getTarget());

    isStationEquals(sourceStation, targetStation);

    Member member = getMember(email);

    Favorite favorite = favoriteRepository.save(new Favorite(sourceStation, targetStation, member.getId()));
    return FavoriteResponse.of(favorite);
  }

  private void isStationEquals(Station source, Station target) {
    if (source.equals(target)) {
      throw new CustomException(StationErrorMessage.STATION_DUPLICATION);
    }
  }

  public List<FavoriteResponse> getFavorite(String email) {
    List<Favorite> favorites = favoriteRepository.findByMemberId(getMember(email).getId());

    return favorites.stream()
        .map(FavoriteResponse::of)
        .collect(toList());
  }

  private Member getMember(String email) {
    return memberService.findByEmail(email);
  }

  @Transactional
  public void deleteFavorite(Long favoriteId) {
    favoriteRepository.deleteById(favoriteId);
  }
}
