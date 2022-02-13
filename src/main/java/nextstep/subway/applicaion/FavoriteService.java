package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

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
        final Member member = memberService.findById(memberId);
        final Station source = stationService.findById(sourceId);
        final Station target = stationService.findById(targetId);
        final Favorite favorite = new Favorite(member, source, target);
        return favoriteRepository.save(favorite).getId();
    }

    public List<Favorite> findAllFavorite(final Long memberId) {
        final Member member = memberService.findById(memberId);
        return favoriteRepository.findByMember(member);
    }

    public void delete(final Long memberId, final Long favoriteId) {
        final Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(EntityNotFoundException::new);
        if (favorite.isMember(memberId)) {
            favoriteRepository.delete(favorite);
            return;
        }
        throw new IllegalArgumentException("it is not member's favorite");
    }
}
