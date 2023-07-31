package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final PathService pathService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, PathService pathService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.pathService = pathService;
        this.memberService = memberService;
    }

    public void saveFavorite(FavoriteSaveRequest favoriteSaveRequest, String email) {

        Member member = memberService.findByEmail(email);

        Station sourceStation = stationService.findById(favoriteSaveRequest.getSource());
        Station targetStation = stationService.findById(favoriteSaveRequest.getTarget());

        pathService.validatePath(sourceStation, targetStation);

        favoriteRepository.save(new Favorite(sourceStation, targetStation, member));
    }

    public List<FavoriteResponse> findFavoritesByEmail(String email) {

        Member member = memberService.findByEmail(email);

        return favoriteRepository.findByMember(member).stream().map(FavoriteResponse::of).collect(Collectors.toList());
    }

    public void deleteById(Long favoriteId, String email) {

        Member member = memberService.findByEmail(email);

        favoriteRepository.findByMember(member)
                .stream()
                .filter(it -> Objects.equals(it.getId(), favoriteId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 즐겨찾기가 해당 사용자에 존재하지 않습니다."));

        favoriteRepository.deleteById(favoriteId);
    }
}
