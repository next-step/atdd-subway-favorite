package nextstep.member.application;

import nextstep.member.domain.*;
import nextstep.member.exception.FavoriteNotFoundException;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
    public Favorite save(LoginMember loginMember, Long sourceId, Long targetId) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail());

        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);

        Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));
        member.addFavorite(favorite);
        return favorite;
    }

    public Favorites findAll(LoginMember loginMember) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail());
        return member.getFavorites();
    }

    @Transactional
    public void delete(LoginMember loginMember, Long favoriteId) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail());
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(FavoriteNotFoundException::new);
        member.removeFavorite(favorite);
    }

}
