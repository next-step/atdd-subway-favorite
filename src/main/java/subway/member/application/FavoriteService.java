package subway.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import subway.auth.principal.UserPrincipal;
import subway.constant.SubwayMessage;
import subway.exception.SubwayNotFoundException;
import subway.member.application.dto.FavoriteCreateRequest;
import subway.member.application.dto.FavoriteCreateResponse;
import subway.member.application.dto.FavoriteRetrieveResponse;
import subway.member.domain.Favorite;
import subway.member.domain.FavoriteRepository;
import subway.member.domain.Member;
import subway.member.domain.MemberFavorites;
import subway.member.domain.MemberRepository;
import subway.path.application.PathService;
import subway.station.application.StationService;
import subway.station.domain.Station;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final StationService stationService;
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final PathService pathService;

    @Transactional(propagation = Propagation.REQUIRED)
    public FavoriteCreateResponse createFavorite(UserPrincipal principal, FavoriteCreateRequest request) {
        Member member = getMember(principal);

        Station sourceStation = stationService.findStationById(request.getSource());
        Station targetStation = stationService.findStationById(request.getTarget());
        pathService.getShortestPath(sourceStation, targetStation);
        Favorite favorite = Favorite.builder()
                .member(member)
                .sourceStation(sourceStation)
                .targetStation(targetStation)
                .build();

        member.appendFavorite(favorite);
        favoriteRepository.save(favorite);

        return FavoriteCreateResponse.from(favorite);
    }

    public List<FavoriteRetrieveResponse> retrieveFavorite(UserPrincipal principal) {
        Member member = getMember(principal);
        MemberFavorites memberFavorites = member.getMemberFavorites();

        return FavoriteRetrieveResponse.from(memberFavorites);
    }

    @Transactional
    public void deleteFavorite(UserPrincipal principal, Long id) {
        Member targetMember = getMember(principal);
        Favorite favorite = findById(id);
        targetMember.deleteFavoriteByFavorite(favorite);
    }

    public Favorite findById(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(() -> new SubwayNotFoundException(SubwayMessage.FAVORITE_NOT_FOUND));
    }

    private Member getMember(UserPrincipal principal) {
        String email = principal.getUsername();
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new SubwayNotFoundException(SubwayMessage.MEMBER_NOT_FOUND));
    }
}
