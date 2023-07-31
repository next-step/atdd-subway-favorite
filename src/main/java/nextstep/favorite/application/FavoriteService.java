package nextstep.favorite.application;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.dto.FavoriteCreateRequest;
import nextstep.favorite.dto.FavoriteResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.MemberNotFoundException;
import nextstep.subway.path.application.PathService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;
    private final PathService pathService;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository, FavoriteRepository favoriteRepository, PathService pathService) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
        this.pathService = pathService;
    }

    @Transactional
    public Long saveFavorite(String email, FavoriteCreateRequest favoriteCreateRequest) {
        Member member = findMember(email);

        Long source = favoriteCreateRequest.getSource();
        Long target = favoriteCreateRequest.getTarget();
        pathService.validatePath(source, target);  // TODO source와 target에 대한 validation을 pathService로 위임했는데, 옳은 선택일지? 이에 대해 어떻게 생각하시는지 알고싶습니다.

        Favorite favorite = saveFavorite(source, target, member);
        return favorite.getId();
    }

    private Favorite saveFavorite(Long source, Long target, Member member) {
        Station sourceStation = findStation(source);
        Station targetStation = findStation(target);

        return favoriteRepository.save(new Favorite(member, sourceStation, targetStation));
    }

    private Station findStation(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(StationNotFoundException::new);
    }

    public List<FavoriteResponse> findAllFavorites(String email) {
        return findMember(email).getFavorites().stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    private Member findMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Transactional
    public void deleteFavorite(String email, Long id) {
        Member member = findMember(email);
        member.removeFavorite(id);
    }
}
