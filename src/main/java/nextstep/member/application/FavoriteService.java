package nextstep.member.application;

import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService, StationService stationService, FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public void createFavorite(String token, FavoriteRequest request) {
        Member member = memberService.findMemberByToken(token);
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());
        Favorite favorite = new Favorite(member, source, target);
        favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> findFavoritesByToken(String token) {
        Member member = memberService.findMemberByToken(token);
        List<Favorite> favorites = favoriteRepository.findByMember(member);
        return createFavoriteResponses(favorites);
    }

    @Transactional
    public void deleteFavorite(String token, Long id) {
        Member member = memberService.findMemberByToken(token);
        favoriteRepository.deleteByMemberAndId(member, id);
    }

    private List<FavoriteResponse> createFavoriteResponses(List<Favorite> favorites) {
        return favorites.stream()
            .map(FavoriteResponse::from)
            .collect(Collectors.toList());
    }
}
