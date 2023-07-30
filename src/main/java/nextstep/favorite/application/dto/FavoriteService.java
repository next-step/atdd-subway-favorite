package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

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

        favoriteRepository.save(Favorite.create(sourceStation, targetStation, member));
    }
}
