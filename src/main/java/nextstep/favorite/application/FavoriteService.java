package nextstep.favorite.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.UserDetails;
import nextstep.exception.ApplicationException;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.repository.StationRepository;
import nextstep.subway.service.PathService;
import org.springframework.stereotype.Service;

import java.util.List;

import static nextstep.exception.ExceptionMessage.NO_EXISTS_STATION_EXCEPTION;
import static nextstep.favorite.application.dto.FavoriteResponse.listFrom;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final PathService pathService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository, PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.pathService = pathService;
    }

    public FavoriteResponse createFavorite(FavoriteRequest request, UserDetails userDetails) {
        Station source = getStation(request.getSource());
        Station target = getStation(request.getTarget());

        // 경로 확인
        pathService.findShortestPath(source.getId(), target.getId());

        Favorite favorite = new Favorite(source, target, getMember(userDetails));
        favoriteRepository.save(favorite);
        return FavoriteResponse.from(favorite);
    }

    public List<FavoriteResponse> findFavorites(UserDetails userDetails) {
        Member member = getMember(userDetails);
        List<Favorite> favorites = favoriteRepository.findByMember(member);
        return listFrom(favorites);
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }

    private Member getMember(UserDetails userDetails) {
        return memberRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(AuthenticationException::new);
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new ApplicationException(NO_EXISTS_STATION_EXCEPTION.getMessage()));
    }
}
