package nextstep.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exception.DuplicateFavoriteException;
import nextstep.favorite.exception.UnauthorizedFavoriteAccessException;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.common.exception.SubwayExceptionType;
import nextstep.subway.path.application.PathService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;
    private final PathService pathService;

    @Transactional
    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Station sourceStation = stationRepository.findByIdOrThrow(request.getSource());
        Station targetStation = stationRepository.findByIdOrThrow(request.getTarget());
        Member member = memberRepository.findByEmailOrElseThrow(loginMember.getEmail());

        validateFavoriteRequest(request, member, sourceStation, targetStation);

        Favorite favorite = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));
        return FavoriteResponse.from(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberRepository.findByEmailOrElseThrow(loginMember.getEmail());

        List<Favorite> favorites = favoriteRepository.findAllByMember(member);

        return favorites.stream()
            .map(FavoriteResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(LoginMember loginMember, Long id) {
        Favorite favorite = favoriteRepository.findByIdOrElseThrow(id);
        Member member = memberRepository.findByEmailOrElseThrow(loginMember.getEmail());

        validateFavoriteOwner(member, favorite);

        favoriteRepository.delete(favorite);
    }

    private void validateFavoriteRequest(FavoriteRequest request, Member member, Station sourceStation, Station targetStation) {
        validatePathExists(request.getSource(), request.getTarget());
        validateDuplicateFavorite(member, sourceStation, targetStation);
    }

    private void validatePathExists(Long sourceId, Long targetId) {
        if (!pathService.existsPath(sourceId, targetId)) {
            throw new SubwayException(SubwayExceptionType.PATH_NOT_FOUND);
        }
    }

    private void validateDuplicateFavorite(Member member, Station sourceStation, Station targetStation) {
        boolean exists = favoriteRepository.existsByMemberAndSourceStationAndTargetStation(
            member, sourceStation, targetStation);
        if (exists) {
            throw new DuplicateFavoriteException(member.getId(), sourceStation.getId(), targetStation.getId());
        }
    }

    private void validateFavoriteOwner(Member member, Favorite favorite) {
        if (!favorite.isOwnedBy(member)) {
            throw new UnauthorizedFavoriteAccessException(member.getId(), favorite.getId());
        }
    }
}
