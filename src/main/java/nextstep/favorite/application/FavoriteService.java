package nextstep.favorite.application;

import nextstep.auth.principal.UserPrincipal;
import nextstep.favorite.application.dto.CreateFavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final PathService pathService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository, PathService pathService, FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.pathService = pathService;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(UserPrincipal userPrincipal, CreateFavoriteRequest createFavoriteRequest) {
        // 멤버 조회
        Member member = memberRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(IllegalArgumentException::new);

        // 역 조회
        Station source = stationRepository.findById(createFavoriteRequest.getSource())
                .orElseThrow(IllegalArgumentException::new);
        Station target = stationRepository.findById(createFavoriteRequest.getTarget())
                .orElseThrow(IllegalArgumentException::new);

        // 경로 검증
        pathService.validationPath(source.getId(), target.getId());

        Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));
        return FavoriteResponse.from(favorite);
    }

    public List<FavoriteResponse> findFavorites(UserPrincipal userPrincipal) {
        return favoriteRepository.findAllByMember_Email(userPrincipal.getUsername()).stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(UserPrincipal userPrincipal, Long id) {
        Favorite favorite = favoriteRepository.findByIdAndMember_Email(id, userPrincipal.getUsername())
                .orElseThrow(IllegalArgumentException::new);

        favoriteRepository.deleteById(favorite.getId());
    }
}
