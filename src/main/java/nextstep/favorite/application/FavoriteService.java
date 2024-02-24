package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.subway.path.PathService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final PathService pathService;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            MemberService memberService,
            PathService pathService
    ) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.pathService = pathService;
    }

    public Long createFavorite(FavoriteRequest request, LoginMember loginMember) {
        verifyDisConnectedStations(request);

        Member member = memberService.findMemberByEmail(loginMember.getEmail());

        Favorite favorite = new Favorite(
                request.getSource(),
                request.getTarget(),
                member.getId()
        );

        return favoriteRepository.save(favorite).getId();
    }

    private void verifyDisConnectedStations(FavoriteRequest request) {
        pathService.getPath(request.getSource(), request.getTarget());
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
