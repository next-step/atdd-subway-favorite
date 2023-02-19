package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    @Transactional
    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
        MemberResponse member = memberService.findMember(memberId);
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());
        Favorite favorite = favoriteRepository.save(new Favorite(member.getId(), source.getId(), target.getId()));
        return createFavoriteResponse(favorite);
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        MemberResponse member = memberService.findMember(memberId);
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());
        return favorites.stream()
            .map(this::createFavoriteResponse)
            .collect(Collectors.toList());
    }

    private FavoriteResponse createFavoriteResponse(Favorite favorite) {
        Long id = favorite.getId();
        Station source = stationService.findById(favorite.getSourceStationId());
        Station target = stationService.findById(favorite.getTargetStationId());
        return new FavoriteResponse(id, StationResponse.of(source), StationResponse.of(target));
    }

    @Transactional
    public void deleteFavorite(Long memberId, Long id) {
        MemberResponse member = memberService.findMember(memberId);
        Favorite favorite = favoriteRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new);

        if (!favorite.getMemberId().equals(member.getId())) {
            throw new RuntimeException();
        }

        favoriteRepository.delete(favorite);
    }
}
