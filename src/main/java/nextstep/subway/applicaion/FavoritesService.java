package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.auth.AuthenticationException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoritesResponse;
import nextstep.subway.domain.Favorites;
import nextstep.subway.domain.FavoritesRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoritesService {

    private final MemberRepository memberRepository;
    private final FavoritesRepository favoritesRepository;
    private final StationService stationService;
    private final PathService pathService;

    public FavoritesService(MemberRepository memberRepository,
        FavoritesRepository favoritesRepository,
        StationService stationService, PathService pathService) {

        this.memberRepository = memberRepository;
        this.favoritesRepository = favoritesRepository;
        this.stationService = stationService;
        this.pathService = pathService;
    }

    @Transactional
    public Long create(FavoriteRequest request, String username) {

        Member member = getMember(username);

        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());

        pathService.findPath(source.getId(), target.getId());

        return favoritesRepository.save(Favorites.of(source, target, member)).getId();
    }

    public Member getMember(String username) {
        return memberRepository.findByEmail(username)
            .orElseThrow(AuthenticationException::new);
    }

    public List<FavoritesResponse> findAllByUser(String username) {

        Member member = getMember(username);

        return favoritesRepository.findAllByMember(member)
            .orElse(List.of())
            .stream()
            .map(FavoritesResponse::of)
            .collect(Collectors.toList());
    }
}
