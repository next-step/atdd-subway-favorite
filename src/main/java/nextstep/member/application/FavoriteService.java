package nextstep.member.application;

import java.util.List;
import java.util.stream.Collectors;
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

    public List<FavoriteResponse> findFavoriteResponses(LoginMember loginMember) {
        Member member = memberService.findByEmail(loginMember.getEmail());
        List<Favorite> favorites = member.getFavorites();

        return favorites.stream()
                .map(it -> FavoriteResponse.of(it, it.getSource(), it.getTarget()))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(LoginMember loginMember, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(IllegalArgumentException::new);

        Member member = memberService.findByEmail(loginMember.getEmail());
        member.removeFavorite(favorite);

        favoriteRepository.delete(favorite);
    }
}
