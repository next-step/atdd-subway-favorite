package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        checkRequest(favoriteRequest);
        Favorite favorite = favoriteRequest.toFavorite();
        member.addFavorite(favorite);

        Map<Long, Station> stations = stationRepository.findAllById(Arrays.asList(favorite.getSourceStationId(), favorite.getTargetStationId()))
                .stream().collect(Collectors.toMap(Station::getId, Function.identity()));

        memberRepository.saveAndFlush(member);
        return FavoriteResponse.of(
                favorite,
                StationResponse.of(stations.get(favorite.getSourceStationId())),
                StationResponse.of(stations.get(favorite.getTargetStationId())));
    }

    private void checkRequest(FavoriteRequest favoriteRequest) {
        stationRepository.findById(favoriteRequest.getSource()).orElseThrow(RuntimeException::new);
        stationRepository.findById(favoriteRequest.getTarget()).orElseThrow(RuntimeException::new);
    }

    @Transactional
    public void deleteFavorite(Long memberId, Long favoriteId) {
        Member member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);

        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(RuntimeException::new);

        member.deleteFavorite(favorite);
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        List<Favorite> favorites = member.findAllFavorite();
        Map<Long, Station> stations = extractStations(favorites);

        return favorites.stream()
                .map(it -> FavoriteResponse.of(
                        it,
                        StationResponse.of(stations.get(it.getSourceStationId())),
                        StationResponse.of(stations.get(it.getTargetStationId()))))
                .collect(Collectors.toList());
    }

    private Map<Long, Station> extractStations(List<Favorite> favorites) {
        Set<Long> stationIds = extractStationIds(favorites);
        return stationRepository.findAllById(stationIds).stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));
    }

    private Set<Long> extractStationIds(List<Favorite> favorites) {
        Set<Long> stationIds = new HashSet<>();
        for (Favorite favorite : favorites) {
            stationIds.add(favorite.getSourceStationId());
            stationIds.add(favorite.getTargetStationId());
        }
        return stationIds;
    }
}
