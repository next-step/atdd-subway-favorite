package nextstep.member.application;

import java.util.List;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService, StationService stationService,
                           FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public Long saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = memberService.findByEmail(loginMember.getEmail());

        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());

        Favorite favorite = member.addFavorite(source, target);

        return favoriteRepository.save(favorite).getId();
    }

    public List<FavoriteResponse> findFavoriteResponses() {
        return null;
    }

    public void deleteFavorite(Long favoriteId) {
        // TODO document why this method is empty
    }


}
