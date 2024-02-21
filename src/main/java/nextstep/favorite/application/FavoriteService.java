package nextstep.favorite.application;

import nextstep.exception.NotFoundException;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.auth.domain.LoginMember;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationRepository stationRepository;
    private MemberService memberService;
    private PathService pathService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository, MemberService memberService, PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberService = memberService;
        this.pathService = pathService;
    }

    public void createFavorite(FavoriteRequest request, LoginMember loginMember) {
        validatePathExist(request);

        final Station sourceStation = stationRepository.findById(request.getSource()).orElseGet(null);
        final Station targetStation = stationRepository.findById(request.getTarget()).orElseGet(null);
        final MemberResponse member = memberService.findMe(loginMember);

        final Favorite favorite = new Favorite(sourceStation, targetStation, member.getId());
        favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        final MemberResponse member = memberService.findMe(loginMember);

        final List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());

        return favorites.stream().map(FavoriteResponse::new).toList();
    }

    public void deleteFavorite(Long id, LoginMember loginMember) {
        final Favorite favorite = favoriteRepository.findById(id).orElseThrow(NotFoundException::new);

        favorite.validateToDelete(memberService.findMe(loginMember).getId());

        favoriteRepository.deleteById(id);
    }

    private void validatePathExist(FavoriteRequest request) {
        pathService.getPath(new PathRequest(request.getSource(), request.getTarget()));
    }
}
