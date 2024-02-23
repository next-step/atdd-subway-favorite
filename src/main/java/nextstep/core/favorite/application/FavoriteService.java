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
import nextstep.core.station.application.StationService;
import nextstep.core.station.application.converter.StationConverter;
import nextstep.core.station.application.dto.StationResponse;
import nextstep.core.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    private final MemberService memberService;

    private final PathFinderService pathFinderService;

    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, PathFinderService pathFinderService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.pathFinderService = pathFinderService;
        this.stationService = stationService;
    }

    @Transactional
    public void createFavorite(FavoriteRequest request, LoginMember loginMember) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail());

        pathFinderService.findShortestPath(new PathFinderRequest(request.getSource(), request.getTarget()));

        Favorite favorite = new Favorite(
                stationService.findStation(request.getSource()),
                stationService.findStation(request.getTarget()),
                member);
        favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail());
        List<Favorite> favorites = favoriteRepository.findByMember(member);

        List<FavoriteResponse> favoriteResponses = new ArrayList<>();

        favorites.forEach(favorite -> {
            favoriteResponses.add(new FavoriteResponse(
                    new StationResponse(favorite.getSourceStation().getId(), favorite.getSourceStation().getName()),
                    new StationResponse(favorite.getTargetStation().getId(), favorite.getTargetStation().getName())));
        });

        return favoriteResponses;
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
