package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.subway.entity.Sections;
import nextstep.subway.entity.Station;
import nextstep.subway.service.LineService;
import nextstep.subway.service.PathFinder;
import nextstep.subway.service.PathService;
import nextstep.subway.service.StationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationService stationService;
    private MemberService memberService;
    private PathService pathService;


    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberService memberService, PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
        this.pathService = pathService;
    }

    /** 즐겨찾기 등록 */
    public void createFavorite(LoginMember loginMember, FavoriteRequest request) {
        verifyConnectedStation(request);

        Member member = memberService.findMemberByEmail(loginMember.getEmail());
        Station sourceStation = stationService.findStation(request.getSource());
        Station targetStation = stationService.findStation(request.getTarget());

        Favorite favorite = new Favorite(member, sourceStation, targetStation);
        favoriteRepository.save(favorite);
    }

    private void verifyConnectedStation(FavoriteRequest request) {
        pathService.getPaths(request.getSource(), request.getTarget());
    }

    /** 즐겨찾기 목록 조회 */
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberEmail(loginMember.getEmail());

        return favorites.stream()
            .map(it -> new FavoriteResponse(it.getId(), it.getSourceStation(), it.getTargetStation()))
            .collect(Collectors.toList());
    }

    /** 즐겨찾기 삭제 */
    public void deleteFavorite(LoginMember loginMember, Long id) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberEmail(loginMember.getEmail());

        Optional<Favorite> deleteFavorite = favorites.stream()
            .filter(it -> it.getId().equals(id))
            .findAny();

        if(deleteFavorite.isEmpty()) {
            throw new IllegalArgumentException("등록하지 않은 즐겨찾기는 삭제할 수 없다");
        }

        favoriteRepository.delete(deleteFavorite.get());
    }
}
