package nextstep.favorite.application;

import java.util.stream.Collectors;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository,
        StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationService = stationService;
    }

    public void createFavorite(FavoriteRequest request, LoginMember member) {
        Member loginMember = getLoginMember(member);

        Favorite favorite = new Favorite(getStationById(request.getSource()), getStationById(
            request.getTarget()), loginMember);

        favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember member) {
        List<Favorite> favorites = favoriteRepository.findAll();

        return favorites.stream()
                                               .map(favorite -> new FavoriteResponse(
                                                   favorite.getId(),
                                                   StationResponse.of(favorite.getSource()),
                                                   StationResponse.of(favorite.getTarget())))
                                               .collect(Collectors.toList());
    }

    public void deleteFavorite(Long id, LoginMember member) {
        Member loginMember = getLoginMember(member);
        Favorite favorite = favoriteRepository.findById(id)
                                              .orElseThrow(IllegalArgumentException::new);

        if(!favorite.isCreateUser(loginMember)) {
            throw new IllegalStateException("본인이 생성한 즐겨찾기만 삭제가 가능합니다.");
        }
        favoriteRepository.deleteById(id);
    }

    private Member getLoginMember(LoginMember member) {
        return memberRepository.findByEmail(member.getEmail())
                               .orElseThrow(
                                   () -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }

    private Station getStationById(Long id) {
        return stationService.findById(id);
    }
}
