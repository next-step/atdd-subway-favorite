package nextstep.subway.applicaion;

import nextstep.auth.authentication.UserDetails;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final MemberService memberService;
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(MemberService memberService, FavoriteRepository favoriteRepository, StationService stationService) {
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(UserDetails userDetails, FavoriteRequest favoriteRequest) {
        Long memberId = memberService.findMember(userDetails.getEmail()).getId();
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());
        Favorite favorite = favoriteRepository.save(Favorite.of(memberId, source, target));
        return FavoriteResponse.from(favorite);
    }
}
