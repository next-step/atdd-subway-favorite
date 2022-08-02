package nextstep.member.application;

import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private MemberService memberService;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public void createFavorite(String loginEmail, Long sourceId, Long targetId) {
        Member loginMember = memberService.findMemberByEmail(loginEmail);
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);

        favoriteRepository.save(Favorite.of(loginMember, source, target));
    }
}
