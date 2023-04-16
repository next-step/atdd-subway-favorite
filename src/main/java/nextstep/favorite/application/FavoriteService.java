package nextstep.favorite.application;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.dto.FavoriteRequestDto;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void create(String email, FavoriteRequestDto favoriteRequestDto) {
        Member member = findMember(email);
        Station sourceStation = stationService.findById(favoriteRequestDto.getSource());
        Station targetStation = stationService.findById(favoriteRequestDto.getTarget());
        Favorite favorite = new Favorite(member, sourceStation, targetStation);
        favoriteRepository.save(favorite);
    }

    private Member findMember(String email) {
        return memberService.findByEmail(email);
    }
}
