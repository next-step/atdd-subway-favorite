package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(final FavoriteRepository favoriteRepository, final MemberService memberService, final StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    @Transactional
    public void saveFavorite(final String email, final FavoriteRequest favoriteRequest) {
        final Member member = memberService.getMember(email);
        final Station sourceStation = stationService.findById(Long.valueOf(favoriteRequest.getSourceStationId()));
        final Station targetStation = stationService.findById(Long.valueOf(favoriteRequest.getTargetStationId()));

        final Favorite favorite = new Favorite(favoriteRepository, member.getId(), sourceStation.getId(), targetStation.getId());
        favoriteRepository.save(favorite);
    }
}
