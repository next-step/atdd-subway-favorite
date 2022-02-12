package nextstep.favorite.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.repository.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.station.application.StationService;
import nextstep.station.domain.Station;

@Transactional
@RequiredArgsConstructor
@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteResponse addFavorite(long memberId, FavoriteRequest request) {
        memberService.verifyExists(memberId);
        stationService.verifyExists(request.getSource());
        stationService.verifyExists(request.getTarget());

        Favorite favorite = Favorite.builder()
            .memberId(memberId)
            .sourceId(request.getSource())
            .targetId(request.getTarget())
            .build();
        favoriteRepository.save(favorite);
        return FavoriteResponse.of(favorite);
    }

    public void deleteFavorite(long memberId, long favoriteId) {
        favoriteRepository.deleteByIdAndMemberId(favoriteId, memberId);
    }

    @Transactional(readOnly = true)
    public List<Favorite> findAllByMember(long memberId) {
        return favoriteRepository.findAllByMemberId(memberId);
    }
}
