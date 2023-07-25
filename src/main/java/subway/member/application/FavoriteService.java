package subway.member.application;

import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.source.spi.EmbeddedAttributeMapping;
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
        // question comment : 이부분에서 repository.save()가 일어나지 않으면 favorite에 insert가 일어나지 않네요.. (다 끝나야 커밋됨)
        // lineSection의 경우 appendSeciton 응답에서 비슷한 로직이 있지만 반환이 void니까 그냥 지나갔는데요.. LineController.appendSection()
        // 이부분은 favorite의 id를 쓰려면 강제로 save로 쿼리를 일으켜야 하나요? 참고로 바로 아래서 트랜잭션을 쪼개서 전파기법으로 해결해보려 했는데 잘 안되네요.
        favoriteRepository.save(favorite);

//        appendFavorite(member, favorite);
        return FavoriteCreateResponse.from(favorite);
    }

//    @Transactional(propagation = Propagation.NESTED)
//    void appendFavorite(Member member, Favorite favorite) {
//        member.appendFavorite(favorite);
//        favoriteRepository.save(favorite);
//    }

    public List<FavoriteRetrieveResponse> retrieveFavorite(UserPrincipal principal) {
        Member member = getMember(principal);
        MemberFavorites memberFavorites = member.getMemberFavorites();

        return FavoriteRetrieveResponse.from(memberFavorites);
    }

    @Transactional
    public void deleteFavorite(UserPrincipal principal, Long id) {
        Member member = getMember(principal);
        Favorite favorite = findById(id);
        member.deleteFavoriteByFavorite(member, favorite);
    }

    public Favorite findById(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(() -> new SubwayNotFoundException(9999L, "즐겨찾기를 찾을 수 없습니다")); // TODO : constant
    }

    private Member getMember(UserPrincipal principal) {
        String email = principal.getUsername();
        // question comment : 이부분은 memberRepository가 맞을까요 memberService가 맞을까요? 일단 memberRepository로 했습니다.
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new SubwayNotFoundException(SubwayMessage.MEMBER_NOT_FOUND));
    }
}
