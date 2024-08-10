package nextstep.favorite.application;

import nextstep.common.exception.MemberNotFoundException;
import nextstep.common.exception.UnauthorizedDeletionException;
import nextstep.common.response.ErrorCode;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.path.application.PathService;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationRepository stationRepository;
    private PathService pathService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository, PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.pathService = pathService;
    }

    /**
     * @param loginMember
     * @param request
     */
    public void createFavorite(LoginMember loginMember, FavoriteRequest request) {
        var member = findMemberByIdOrThrow(loginMember);

        // 비정상 경로 확인
        pathService.getShortestPath(request.getSource(), request.getTarget());

        Favorite favorite = new Favorite(member.getId(), request.getSource(), request.getTarget());
        favoriteRepository.save(favorite);
    }

    /**
     * @param loginMember
     * @return
     */
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        var member = findMemberByIdOrThrow(loginMember);
        List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());
        return favorites.stream()
                .map(this::createFavoriteResponse)
                .collect(Collectors.toList());
    }

    /**
     * @param loginMember
     * @param id
     */
    public void deleteFavorite(LoginMember loginMember, Long id) {
        var member = findMemberByIdOrThrow(loginMember);
        var favorite = favoriteRepository.findById(id).orElseThrow();
        if(!favorite.getMemberId().equals(member.getId())) {
            throw new UnauthorizedDeletionException(ErrorCode.UNATHORIZED_FAVORITE_DELETE);
        }

        favoriteRepository.deleteById(id);
    }
    private Member findMemberByIdOrThrow(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
    }
    private FavoriteResponse createFavoriteResponse(Favorite favorite) {
        Station sourceStation = stationRepository.findById(favorite.getSourceStationId()).orElseThrow();
        Station targetStation = stationRepository.findById(favorite.getTargetStationId()).orElseThrow();
        return FavoriteResponse.of(favorite, sourceStation, targetStation);
    }
}
