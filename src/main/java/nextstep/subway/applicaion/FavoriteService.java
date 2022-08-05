package nextstep.subway.applicaion;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.member.domain.User;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
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
  public FavoriteResponse saveFavorite(FavoriteRequest favoriteRequest, User user) {
    Station sourceStation = stationService.findById(favoriteRequest.getSource());
    Station targetStation = stationService.findById(favoriteRequest.getTarget());

    if (sourceStation.equals(targetStation)) {
      throw new CustomException(StationErrorMessage.STATION_DUPLICATION);
    }

    MemberResponse member = memberService.findMember(user.getEmail());

    Favorite favorite = favoriteRepository.save(new Favorite(sourceStation, targetStation, member.getId()));
    return FavoriteResponse.of(favorite);
  }

  public List<FavoriteResponse> getFavorite(User user) {
    MemberResponse member = memberService.findMember(user.getEmail());

    List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());

    return favorites.stream()
        .map(FavoriteResponse::of)
        .collect(toList());
  }

  @Transactional
  public void deleteFavorite(Long favoriteId) {
    favoriteRepository.deleteById(favoriteId);
  }
}
