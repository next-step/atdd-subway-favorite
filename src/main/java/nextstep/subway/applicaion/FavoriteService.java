package nextstep.subway.applicaion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    @Transactional
    public Long addFavorite(String email, Long source, Long target) {
        Member member = memberService.findMemberByEmail(email);

        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);

        return favoriteRepository.save(new Favorite(member.getId(), sourceStation.getId(), targetStation.getId())).getId();
    }
}
