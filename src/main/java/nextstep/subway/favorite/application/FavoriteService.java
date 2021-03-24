package nextstep.subway.favorite.application;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.domain.Favorites;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FavoriteService {

  private FavoriteRepository favoriteRepository;
  private MemberRepository memberRepository;
  private StationRepository stationRepository;

  public FavoriteService(
      FavoriteRepository favoriteRepository,
      MemberRepository memberRepository,
      StationRepository stationRepository)
  {
    this.memberRepository = memberRepository;
    this.favoriteRepository = favoriteRepository;
    this.stationRepository = stationRepository;
  }

  public Favorite add(FavoriteRequest favoriteRequest, long id) {
    Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
    Station source = stationRepository.findById(favoriteRequest.getSource()).orElseThrow(RuntimeException::new);
    Station target = stationRepository.findById(favoriteRequest.getTarget()).orElseThrow(RuntimeException::new);
    return member.addFavorite(new Favorite(member,source,target));
  }

  public void remove(long id,long favoriteId) {
    Favorites favorites =  memberRepository.findById(id)
        .orElseThrow(RuntimeException::new)
        .getFavorites();
    Favorite target = favorites.getAllFavorite().stream()
        .filter(favorite -> favorite.getId().equals(favoriteId))
        .findFirst()
        .orElseThrow(RuntimeException::new);
    favorites.remove(target);
  }

  public List<FavoriteResponse> findAll(long id){
    Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
    return member.getFavorites()
        .getAllFavorite()
        .stream()
        .map(FavoriteResponse::of)
        .collect(Collectors.toList());
  }
}
