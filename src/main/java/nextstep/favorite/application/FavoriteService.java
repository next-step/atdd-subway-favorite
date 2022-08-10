package nextstep.favorite.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.application.dto.FavoriteStationResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exception.NotOwnerFavoriteException;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteService {
    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public Favorite saveFavorite(String email, FavoriteRequest favoriteRequest) {
        Long memberId = memberService.findMember(email).getId();

        Station sourceStation = stationService.findById(favoriteRequest.getSource());
        Station targetStation = stationService.findById(favoriteRequest.getTarget());

        Favorite favorite = new Favorite(memberId, sourceStation, targetStation);

        return favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> findFavorites(String email) {
        Long memberId = memberService.findMember(email).getId();

        return favoriteRepository.findByMemberId(memberId).stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(String email, Long id) {
        Favorite favorite = favoriteRepository.findById(id)
            .orElseThrow();

        Long memberId = memberService.findMember(email).getId();

        if(!favorite.isOwner(memberId)) {
            throw new NotOwnerFavoriteException("자신의 즐겨찾기만 제거가 가능합니다.");
        }
        favoriteRepository.deleteById(id);
    }

}
