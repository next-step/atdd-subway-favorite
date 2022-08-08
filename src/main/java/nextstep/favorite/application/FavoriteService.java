package nextstep.favorite.application;

import java.util.List;
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
    public FavoriteResponse saveFavorite(String email, FavoriteRequest favoriteRequest) {
        Long memberId = memberService.findMember(email).getId();

        Favorite favorite = new Favorite(memberId, favoriteRequest.getSource(), favoriteRequest.getTarget());
        Favorite saveFavorite = favoriteRepository.save(favorite);

        return createFavoriteResponse(saveFavorite);
    }

    private FavoriteResponse createFavoriteResponse(Favorite favorite) {
        Station source = stationService.findById(favorite.getSource());
        Station target = stationService.findById(favorite.getTarget());

        return new FavoriteResponse(
            favorite.getId(),
            new FavoriteStationResponse(source.getId(), source.getName()),
            new FavoriteStationResponse(target.getId(), target.getName()));
    }

    public List<FavoriteResponse> findFavorites(String email) {
        Long memberId = memberService.findMember(email).getId();

        return favoriteRepository.findByMemberId(memberId).stream()
            .map(favorite -> createFavoriteResponse(favorite))
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
