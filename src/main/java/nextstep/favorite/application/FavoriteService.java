package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteCreateRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.AuthenticationException;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.path.exception.PathException;
import nextstep.subway.path.service.PathService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.service.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationService stationService;
    private final PathService pathService;

    public FavoriteService(
        FavoriteRepository favoriteRepository,
        MemberRepository memberRepository,
        StationService stationService,
        PathService pathService
    ) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationService = stationService;
        this.pathService = pathService;
    }

    /**
     * 주어진 로그인 회원 정보와, 즐겨찾기 추가 요청 정보를 이용해 즐겨찾기 구간으로 등록합니다.
     *
     * @param loginMember 로그인한 회원 정보
     * @param request     즐겨찾기 추가 요청 정보
     * @return 추가된 즐겨찾기의 식별자
     */
    @Transactional
    public long createFavorite(LoginMember loginMember, FavoriteCreateRequest request) {
        Member member = findMember(loginMember);

        Station sourceStation = stationService.findByStationId(request.getSource());
        Station targetStation = stationService.findByStationId(request.getTarget());

        pathService.getPaths(sourceStation.getId(), targetStation.getId());

        return favoriteRepository.save(new Favorite(member.getId(), sourceStation.getId(), targetStation.getId())).getId();
    }

    /**
     * 주어진 로그인 회원정보를 통해 찾은 즐겨찾기 목록을 반환하니다.
     *
     * @return 조회한 즐겨찾기 목록
     */
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = findMember(loginMember);
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());

        return favorites.stream()
            .map(favorite -> FavoriteResponse.of(
                favorite.getId(),
                stationService.findByStationId(favorite.getSourceStationId()),
                stationService.findByStationId(favorite.getTargetStationId())
            ))
            .collect(Collectors.toList());
    }

    /**
     * 주어진 로그인 회원정보와 삭제할 즐겨찾기 식별자를 이용해 해당하는 즐겨찾기를 제거합니다.
     *
     * @param loginMember 로그인한 회원 정보
     * @param id          즐겨찾기 식별자
     */
    @Transactional
    public void deleteFavorite(LoginMember loginMember, Long id) {
        Member member = findMember(loginMember);
        Favorite favorite = favoriteRepository.findByIdAndMemberId(id, member.getId())
            .orElseThrow(EntityNotFoundException::new);

        favoriteRepository.deleteById(favorite.getId());
    }

    private Member findMember(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail())
            .orElseThrow(AuthenticationException::new);
    }
}
