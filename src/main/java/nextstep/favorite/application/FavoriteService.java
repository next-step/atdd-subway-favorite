package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.subway.application.PathService;
import nextstep.subway.application.dto.PathResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberService memberService;
    private PathService pathService;

    public FavoriteService(FavoriteRepository favoriteRepository, final MemberService memberService,
                           final PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.pathService = pathService;
    }

    /**
     * TODO: LoginMember 를 추가로 받아서 FavoriteRequest 내용과 함께 Favorite 를 생성합니다.
     *
     * @param loginMember
     * @param request
     */
    public FavoriteResponse createFavorite(final LoginMember loginMember, FavoriteRequest request) {
        final Member member = memberService.findMemberEntityByEmail(loginMember.getEmail());
        final Long sourceId = request.getSource();
        final Long targetId = request.getTarget();
        final PathResponse pathResponse = pathService.findPath(sourceId, targetId);

        Favorite favorite = new Favorite(member.getId(), sourceId, targetId);
        final Favorite savedFavorite = favoriteRepository.save(favorite);
        return new FavoriteResponse(savedFavorite, pathResponse);
    }

    /**
     * TODO: StationResponse 를 응답하는 FavoriteResponse 로 변환해야 합니다.
     *
     * @return
     */
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        final Member member = memberService.findMemberEntityByEmail(loginMember.getEmail());
        return favoriteRepository.findAllByMemberId(member.getId());
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     * @param id
     */
    public void deleteFavorite(LoginMember loginMember, Long id) {
        final Member member = memberService.findMemberEntityByEmail(loginMember.getEmail());
        favoriteRepository.deleteById(id);
    }
}
