package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public long createFavorite(FavoriteRequest favoriteRequest, Long memberId) {
        Station sourceStation = stationService.findById(Long.parseLong(favoriteRequest.getSource()));
        Station targetStation = stationService.findById(Long.parseLong(favoriteRequest.getTarget()));
        Member member = memberService.findById(memberId);

        Favorite favorite = favoriteRepository.save(new Favorite(sourceStation, targetStation, member));
        return favorite.getId();
    }

    public List<FavoriteResponse> getFavorites(Long id) {
        List<Favorite> favorites = favoriteRepository.findFavoriteByMember(memberService.findById(id));
        return favorites.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }
}
