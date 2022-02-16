package nextstep.subway.applicaion;

import nextstep.auth.authentication.UserDetails;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationService stationService;
    private MemberRepository memberRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
    }

    public FavoriteResponse saveFavorite(FavoriteRequest favoriteRequest, long memberId) {
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());
        Member member = memberRepository.findById(memberId)
            .orElseThrow(IllegalArgumentException::new);

        boolean isDuplicated = favoriteRepository.existsFavoriteBySourceIdAndTargetId(source.getId(), target.getId());
        if (isDuplicated) {
            throw new IllegalArgumentException();
        }

        Favorite favorite = favoriteRepository.save(new Favorite(source, target, member));

        return FavoriteResponse.of(favorite);
    }
}
