package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(final FavoriteRepository favoriteRepository, final MemberService memberService, final StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    @Transactional
    public Long saveFavorite(final String email, final FavoriteRequest favoriteRequest) {
        final Member member = memberService.getMember(email);
        final Station sourceStation = stationService.findById(Long.valueOf(favoriteRequest.getSourceStationId()));
        final Station targetStation = stationService.findById(Long.valueOf(favoriteRequest.getTargetStationId()));

        final Favorite favorite = new Favorite(favoriteRepository, member.getId(), sourceStation.getId(), targetStation.getId());
        favoriteRepository.save(favorite);
        return favorite.getId();
    }

    public List<FavoriteResponse> findFavorites(final String email) {
        final Member member = memberService.getMember(email);
        final List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());
        return createFavoriteResponses(favorites);
    }

    @Transactional
    public void deleteFavorite(final String email, final Long favoriteId) {
        final Member member = memberService.getMember(email);
        final Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(RuntimeException::new);
        if (!favorite.getMemberId().equals(member.getId())) {
            throw new RuntimeException("해당 즐겨찾기의 등록자가 아닙니다.");
        }
        favoriteRepository.delete(favorite);
    }

    private List<FavoriteResponse> createFavoriteResponses(final List<Favorite> favorites) {
        return favorites.stream()
                .map(favorite -> {
                    final Station sourceStation = stationService.findById(favorite.getSourceStationId());
                    final Station targetStation = stationService.findById(favorite.getTargetStationId());
                    return new FavoriteResponse(favorite.getId(), sourceStation, targetStation);
                })
                .collect(Collectors.toList());
    }
}
