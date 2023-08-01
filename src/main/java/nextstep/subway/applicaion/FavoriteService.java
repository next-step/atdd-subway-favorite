package nextstep.subway.applicaion;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationService stationService;

    public FavoriteService(final FavoriteRepository favoriteRepository, final MemberRepository memberRepository,
                           final StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationService = stationService;
    }

    public Favorite createFavorite(FavoriteRequest favoriteRequest, String userEmail) {
        Long memberId = getMemberIdByEmail(userEmail);
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());
        Favorite favorite = Favorite.of(memberId, source, target);
        favoriteRepository.save(favorite);
        return favorite;
    }

    public List<FavoriteResponse> findFavorites(String userEmail) {
        Long memberId = getMemberIdByEmail(userEmail);
        List<Favorite> favorites = favoriteRepository.findByMemberId(memberId);
        return favorites.stream().map(this::createFavoriteResponse).collect(Collectors.toList());
    }

    public void deleteFavorite(Long id, String userEmail) {
        Long memberId = getMemberIdByEmail(userEmail);
        favoriteRepository.findByMemberId(memberId)
                .stream().filter(f -> f.getId().equals(id))
                .findFirst().ifPresent(f -> favoriteRepository.deleteById(id));
    }

    private Long getMemberIdByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(UnauthorizedException::new).getId();
    }

    private FavoriteResponse createFavoriteResponse(Favorite favorite) {
        return FavoriteResponse.of(favorite.getId(),
                stationService.createStationResponse(favorite.getSource()),
                stationService.createStationResponse(favorite.getTarget()));
    }
}
