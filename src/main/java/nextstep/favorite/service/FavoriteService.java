package nextstep.favorite.service;

import lombok.RequiredArgsConstructor;
import nextstep.favorite.adapters.persistence.FavoriteJpaAdapter;
import nextstep.favorite.dto.request.FavoriteRequest;
import nextstep.favorite.dto.response.FavoriteResponse;
import nextstep.favorite.entity.Favorite;
import nextstep.global.error.code.ErrorCode;
import nextstep.global.error.exception.InvalidFavoriteException;
import nextstep.global.error.exception.InvalidPathException;
import nextstep.member.adapters.persistence.MemberJpaAdapter;
import nextstep.member.entity.Member;
import nextstep.subway.path.service.PathService;
import nextstep.subway.station.adapters.persistence.StationJpaAdapter;
import nextstep.subway.station.entity.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final MemberJpaAdapter memberJpaAdapter;

    private final StationJpaAdapter stationJpaAdapter;

    private final FavoriteJpaAdapter favoriteJpaAdapter;

    private final PathService pathService;

    @Transactional
    public FavoriteResponse saveFavorite(String email, FavoriteRequest favoriteRequest) {
        if (favoriteRequest.isSameSourceAndTarget()) {
            throw new InvalidFavoriteException(ErrorCode.SAME_DEPARTURE_AND_ARRIVAL_STATIONS);
        }

        Station source = stationJpaAdapter.findById(favoriteRequest.getSource());
        Station target = stationJpaAdapter.findById(favoriteRequest.getTarget());

        validateConnectedPath(source, target);

        Member member = memberJpaAdapter.findByEmail(email);
        Favorite favorite = Favorite.builder()
                .memberId(member.getId())
                .source(source)
                .target(target)
                .build();

        Favorite savedFavorite = favoriteJpaAdapter.save(favorite);

        return FavoriteResponse.of(savedFavorite);
    }

    public List<FavoriteResponse> findAllFavoritesByEmail(String email) {
        Member member = memberJpaAdapter.findByEmail(email);
        List<Favorite> favorites = favoriteJpaAdapter.findByMemberId(member.getId());

        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavoriteByFavoriteId(Long favoriteId, String email) {
        Member member = memberJpaAdapter.findByEmail(email);
        favoriteJpaAdapter.deleteByIdAndMemberId(favoriteId, member.getId());
    }

    /**
     * 연결되어 있지 않을 경우
     * {@link InvalidPathException} 발생
     *
     * @param source 출발역
     * @param target 도착역
     * @exception InvalidPathException
     */
    private void validateConnectedPath(Station source, Station target) {
        pathService.getPathWithDijkstra(source, target);
    }
}
