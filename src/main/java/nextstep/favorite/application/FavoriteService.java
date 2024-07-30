package nextstep.favorite.application;

import lombok.RequiredArgsConstructor;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.common.FavoriteErrorMessage;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exception.NoFavoriteException;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.subway.path.service.PathService;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final PathService pathService;
    private final MemberService memberService;

    public Favorite createFavorite(FavoriteRequest request, LoginMember loginMember) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail());
        pathService.findShortestPath(request.getSource(), request.getTarget());
        Favorite favorite = new Favorite(member.getId(), request.getSource(), request.getTarget());

        return favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail());
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());

        List<Long> stationIds = favorites.stream().flatMap(favorite -> Stream.of(favorite.getSourceStationId(), favorite.getTargetStationId()))
                .distinct()
                .collect(Collectors.toList());

        List<Station> favoriteStations = stationRepository.findByIdIn(stationIds);

        return favorites.stream().map(favorite -> FavoriteResponse.from(favorite, favoriteStations))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(LoginMember loginMember, Long favoriteId) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail());

        Favorite favorite = favoriteRepository.findByIdAndMemberId(favoriteId, member.getId())
                .orElseThrow(() -> new NoFavoriteException(FavoriteErrorMessage.NO_FAVORITE_EXIST));

        favoriteRepository.delete(favorite);
    }
}
