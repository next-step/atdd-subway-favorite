package nextstep.favorite.application;

import lombok.RequiredArgsConstructor;
import nextstep.common.MemberNotFoundException;
import nextstep.favorite.presentation.FavoriteRequest;
import nextstep.favorite.presentation.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.infrastructure.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.member.infrastructure.MemberRepository;
import nextstep.subway.domain.Station;
import nextstep.common.StationNotFoundException;
import nextstep.subway.infrastructure.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    public Long createFavorite(FavoriteRequest request, LoginMember loginMember) {
        Station sourceStation = findStationByIdOrThrow(request.getSourceStationId());
        Station targetStation = findStationByIdOrThrow(request.getTargetStationId());
        memberRepository.findByEmail(loginMember.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(loginMember.getEmail()));

        Favorite favorite = Favorite.of(
                sourceStation.getId(),
                targetStation.getId(),
                loginMember.getId()
        );

        Favorite createdFavorite = favoriteRepository.save(favorite);
        return createdFavorite.getId();
    }

    /**
     * TODO: StationResponse 를 응답하는 FavoriteResponse 로 변환해야 합니다.
     *
     * @return
     */
    public List<FavoriteResponse> findFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();
        return null;
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     * @param id
     */
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }

    private Station findStationByIdOrThrow(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(stationId));
    }
}
