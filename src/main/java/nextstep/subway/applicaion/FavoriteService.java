package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.FavoriteCreateRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(
            final MemberService memberService,
            final StationService stationService,
            final FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public List<FavoriteResponse> findFavoriteResponses(final String email) {
        Member member = memberService.findMemberByEmail(email);

        return FavoriteResponse.fromList(member.getFavoriteList());
    }

    @Transactional
    public Long createFavorite(final String email, final FavoriteCreateRequest favoriteCreateRequest) {
        Member member = memberService.findMemberByEmail(email);
        Station sourceStation = stationService.findById(favoriteCreateRequest.getSourceId());
        Station targetStation = stationService.findById(favoriteCreateRequest.getTargetId());
        Favorite favorite = Favorite.builder()
                .member(member)
                .sourceStation(sourceStation)
                .targetStation(targetStation)
                .build();

        favoriteRepository.save(favorite);

        return favorite.getId();
    }

    @Transactional
    public void deleteFavorite(final String email, final Long favoriteId) {
        Member member = memberService.findMemberByEmail(email);
        Favorite favorite = findFavoriteById(favoriteId);

        member.deleteFavorite(favorite);
    }

    private Favorite findFavoriteById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
