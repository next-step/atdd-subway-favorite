package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.domain.Favorites;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FavoriteService {

  private MemberService memberService;
  private StationService stationService;
  private FavoriteRepository favoriteRepository;

  public FavoriteService(
      MemberService memberService,
      StationService stationService,
      FavoriteRepository favoriteRepository
  ) {
    this.memberService = memberService;
    this.stationService = stationService;
    this.favoriteRepository = favoriteRepository;
  }

  public Favorite add(FavoriteRequest favoriteRequest, long id) {
    Member member = memberService.findMemberDomain(id);
    Station source = stationService.findById(favoriteRequest.getSource());
    Station target = stationService.findById(favoriteRequest.getTarget());
    return member.addFavorite(new Favorite(id, source.getId(), target.getId()));
  }

  public void remove(long favoriteId) {
      favoriteRepository.deleteById(favoriteId);
  }

  public List<FavoriteResponse> findAll(long id) {
    Member member = memberService.findMemberDomain(id);
    return member.getFavorites()
        .getAllFavorite()
        .stream()
        .map(favorite ->
            FavoriteResponse.of(
                favorite,
                stationService.findById(favorite.getSourceId()),
                stationService.findById(favorite.getTargetId())
            )
        )
        .collect(Collectors.toList());
  }
}
