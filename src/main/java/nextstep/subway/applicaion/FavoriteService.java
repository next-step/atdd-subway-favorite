package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.domain.*;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        return favoriteRepository.save(new Favorite(member, source, target));
    }

    public List<Favorite> findAll(LoginMember loginMember) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail());
        return favoriteRepository.findByMemberId(member.getId());
    }

    @Transactional
    public void delete(LoginMember loginMember, Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }

}
