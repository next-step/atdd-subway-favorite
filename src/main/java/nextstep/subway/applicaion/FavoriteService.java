package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class FavoriteService {

    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService, StationService stationService, FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public Long createFavorite(Long userId, Long source, Long target) {
        Long memberId = memberService.findMember(userId).getId();
        Long sourceId = stationService.findById(source).getId();
        Long targetId = stationService.findById(target).getId();
        Favorite favorite = favoriteRepository.save(new Favorite(memberId, sourceId, targetId));
        return favorite.getId();
    }

    public List<FavoriteResponse> loadFavorites(Long userId) {
        Long memberId = memberService.findMember(userId).getId();
        return favoriteRepository.findAllByMemberId(memberId).stream().map(favorite -> {
            Station source = stationService.findById(favorite.getSourceId());
            Station target = stationService.findById(favorite.getTargetId());
            return FavoriteResponse.make(favorite.getId(), StationResponse.from(source), StationResponse.from(target));
        }).collect(Collectors.toList());
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        favoriteRepository.deleteByIdAndMemberId(memberId, favoriteId);
    }

}
