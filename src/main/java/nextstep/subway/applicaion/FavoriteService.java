package nextstep.subway.applicaion;

import static java.util.stream.Collectors.toList;

import java.util.List;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.User;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
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

    sourceStation.isStationEquals(targetStation);

    MemberResponse member = memberService.findMember(user.getEmail());

    Favorite favorite = favoriteRepository.save(new Favorite(sourceStation, targetStation, member.getId()));
    return FavoriteResponse.of(favorite);
  }

  public List<FavoriteResponse> getFavorite(User user) {
    List<Favorite> favorites = favoriteRepository.findByMemberId(getMemberResponse(user.getEmail()).getId());

    return favorites.stream()
        .map(FavoriteResponse::of)
        .collect(toList());
  }

  private MemberResponse getMemberResponse(String email) {
    return memberService.findMember(email);
  }

  @Transactional
  public void deleteFavorite(Long favoriteId) {
    favoriteRepository.deleteById(favoriteId);
  }
}
