package nextstep.favorite.application;

import javax.persistence.EntityNotFoundException;
import nextstep.exception.NotFoundException;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
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

    /**
     * TODO: LoginMember 를 추가로 받아서 FavoriteRequest 내용과 함께 Favorite 를 생성합니다.
     *
     * @param request
     * @param loginMember
     */
    public void createFavorite(FavoriteRequest request, LoginMember loginMember) {
        validatePathExist(request);

        final Station sourceStation = stationRepository.findById(request.getSource()).orElseGet(null);
        final Station targetStation = stationRepository.findById(request.getTarget()).orElseGet(null);
        final MemberResponse member = memberService.findMe(loginMember);

        final Favorite favorite = new Favorite(sourceStation, targetStation, member.getId());
        favoriteRepository.save(favorite);
    }

    /**
     * TODO: StationResponse 를 응답하는 FavoriteResponse 로 변환해야 합니다.
     *
     * @return
     */
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        final MemberResponse member = memberService.findMe(loginMember);

        final List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());

        return favorites.stream().map(FavoriteResponse::new).toList();
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     *
     * @param id
     * @param loginMember
     */
    public void deleteFavorite(Long id, LoginMember loginMember) {
        final Favorite favorite = favoriteRepository.findById(id).orElseThrow(NotFoundException::new);

        favorite.validateToDelete(memberService.findMe(loginMember).getId());

        favoriteRepository.deleteById(id);
    }

    private void validatePathExist(FavoriteRequest request) {
        pathService.getPath(new PathRequest(request.getSource(), request.getTarget()));
    }
}
