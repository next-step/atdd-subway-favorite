package nextstep.subway.applicaion;

import nextstep.auth.AuthenticationException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.domain.Favorites;
import nextstep.subway.domain.FavoritesRepository;
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

        Member member = memberRepository.findByEmail(username)
            .orElseThrow(AuthenticationException::new);

        Long source = stationService.findById(request.getSource()).getId();
        Long target = stationService.findById(request.getTarget()).getId();

        pathService.findPath(source, target);

        return favoritesRepository.save(Favorites.of(source, target, member)).getId();
    }
}
