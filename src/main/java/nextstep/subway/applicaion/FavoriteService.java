package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationService stationService;
    private MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoriteResponse saveFavorite(FavoriteRequest favoriteRequest, long memberId) {
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());
        Member member = memberService.findById(memberId);

        boolean isDuplicated = favoriteRepository.existsFavoriteBySourceAndTarget(source, target);
        if (isDuplicated) {
            throw new IllegalArgumentException();
        }

        Favorite favorite = favoriteRepository.save(new Favorite(source, target, member));

        return FavoriteResponse.of(favorite);
    }

    public void deleteFavorite(Long id, Long memberId) {
        favoriteRepository.deleteByIdAndMemberId(id, memberId);
    }

    public List<FavoriteResponse> getFavorites(Long memberId) {
        return favoriteRepository.findAllByMemberId(memberId).stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }
}
