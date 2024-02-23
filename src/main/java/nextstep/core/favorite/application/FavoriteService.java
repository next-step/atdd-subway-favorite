package nextstep.core.favorite.application;

import nextstep.core.favorite.application.dto.FavoriteRequest;
import nextstep.core.favorite.application.dto.FavoriteResponse;
import nextstep.core.favorite.domain.Favorite;
import nextstep.core.favorite.domain.FavoriteRepository;
import nextstep.core.member.application.MemberService;
import nextstep.core.member.domain.LoginMember;
import nextstep.core.member.domain.Member;
import nextstep.core.pathFinder.application.PathFinderService;
import nextstep.core.pathFinder.application.dto.PathFinderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    private final MemberService memberService;

    private final PathFinderService pathFinderService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, PathFinderService pathFinderService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.pathFinderService = pathFinderService;
    }

    @Transactional
    public void createFavorite(FavoriteRequest request, LoginMember loginMember) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail());

        pathFinderService.findShortestPath(new PathFinderRequest(request.getSource(), request.getTarget()));

        Favorite favorite = new Favorite(member);
        favoriteRepository.save(favorite);
    }

    /**
     * TODO: StationResponse 를 응답하는 FavoriteResponse 로 변환해야 합니다.
     *
     * @return
     */
    public List<FavoriteResponse> findFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();
        return null;
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     *
     * @param id
     */
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
