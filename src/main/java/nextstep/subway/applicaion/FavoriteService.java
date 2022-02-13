package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(final FavoriteRepository favoriteRepository, final MemberService memberService, final StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public Long createFavorite(final Long memberId, final Long sourceId, final Long targetId) {
        final MemberResponse member = memberService.findMember(memberId);
        final Station source = stationService.findById(sourceId);
        final Station target = stationService.findById(targetId);
        final Favorite favorite = new Favorite(member.getId(), source.getId(), target.getId());
        return favoriteRepository.save(favorite).getId();
    }
}
