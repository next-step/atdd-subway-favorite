package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(
        FavoriteRepository favoriteRepository,
        MemberService memberService,
        StationService stationService
    ) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    @Transactional
    public FavoriteResponse create(String email, Long targetId, Long sourceId) {
        final Member member = memberService.findMemberByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException());
        final Station target = stationService.findById(targetId);
        final Station source = stationService.findById(sourceId);

        final Favorite favorite = favoriteRepository.save(new Favorite(member.getId(), source, target));
        return FavoriteResponse.toDto(favorite);
    }

    public FavoriteResponse findById(Long favoriteId, String email) {
        final Favorite favorite = favoriteRepository.findById(favoriteId)
            .orElseThrow(() -> new IllegalArgumentException());
        final Member member = memberService.findMemberByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException());

        if (!favorite.isSameMember(member.getId())) {
            throw new IllegalArgumentException();
        }
        return FavoriteResponse.toDto(favorite);
    }
}
