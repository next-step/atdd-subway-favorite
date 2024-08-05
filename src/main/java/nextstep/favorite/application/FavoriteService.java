package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.exception.UnAuthorizedException;
import nextstep.path.service.PathFinder;
import nextstep.station.entity.Station;
import nextstep.station.service.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nextstep.common.constant.ErrorCode.UNAUTHORIZED_ACCESS;
import static nextstep.converter.FavoriteConverter.favoritesToFavoriteResponses;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberService memberService;
    private StationService stationService;
    private PathFinder pathFinder;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService, PathFinder pathFinder) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    @Transactional
    public Long createFavorite(final LoginMember loginMember, final FavoriteRequest favoriteRequest) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail());
        pathFinder.retrieveStationPath(favoriteRequest.getSource(), favoriteRequest.getTarget());
        Station sourceStation = stationService.getStationByIdOrThrow(favoriteRequest.getSource());
        Station targetStation = stationService.getStationByIdOrThrow(favoriteRequest.getTarget());
        Favorite favorite = Favorite.of(member, sourceStation, targetStation);

        return favoriteRepository.save(favorite).getId();
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(final LoginMember loginMember) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail());
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());

        return favoritesToFavoriteResponses(favorites);
    }

    @Transactional
    public void deleteFavorite(final LoginMember loginMember, final Long id) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail());
        Favorite favorite = favoriteRepository.findByIdAndMemberId(id, member.getId()).orElseThrow(
                () -> new UnAuthorizedException(String.valueOf(UNAUTHORIZED_ACCESS))
        );
        favoriteRepository.deleteById(favorite.getId());
    }
}
