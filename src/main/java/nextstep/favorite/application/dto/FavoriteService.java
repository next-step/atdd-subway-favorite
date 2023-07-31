package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final StationService stationService;
    private final PathService pathService;
    private final MemberService memberService;

    public FavoriteService( StationService stationService, PathService pathService, MemberService memberService) {
        this.stationService = stationService;
        this.pathService = pathService;
        this.memberService = memberService;
    }

    @Transactional
    public void saveFavorite(FavoriteSaveRequest favoriteSaveRequest, String email) {

        Member member = memberService.findByEmail(email);

        Station sourceStation = stationService.findById(favoriteSaveRequest.getSource());
        Station targetStation = stationService.findById(favoriteSaveRequest.getTarget());

        pathService.validatePath(sourceStation, targetStation);

        member.addFavorite(new Favorite(sourceStation, targetStation, member));
    }

    public List<FavoriteResponse> findFavoritesByEmail(String email) {

        Member member = memberService.findByEmail(email);

        return member.getFavorites().stream().map(FavoriteResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(Long favoriteId, String email) {

        Member member = memberService.findByEmail(email);

        Favorite favorite = member.getFavorites().stream()
                .filter(it -> Objects.equals(it.getId(), favoriteId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 즐겨찾기가 해당 사용자에 존재하지 않습니다."));

        member.removeFavorite(favorite);
    }
}
