package nextstep.member.application;

import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.*;

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

    public List<FavoriteResponse> getFavorites(String email) {
        Member findMember = memberService.findMemberByEmail(email);
        List<Favorite> findFavorites = favoriteRepository.findFavoritesByMemberId(findMember.getId());

        return findFavorites.stream()
                .map(this::createFavoriteResponse)
                .collect(toList());
    }

    public FavoriteResponse createFavoriteResponse(Favorite favorite) {
        return FavoriteResponse.of(favorite.getId(), favorite.getSource(), favorite.getTarget());
    }

}
